package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.carpooling.ums.dto.EmergencyContactDTO;
import com.carpooling.ums.dto.UserDetailsDTO;
import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.services.S3Service;
import com.carpooling.ums.services.UserDetailsService;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;
import com.carpooling.ums.utils.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/userdetails")
public class UserDetailsController {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsController.class);
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
	private S3Service s3Service;


	@GetMapping
	public ResponseEntity<ApiResponse<List<UserDetailsDTO>>> getAllUserDetails() {
		try {
			// Test Comment
			List<UserDetails> userDetailsList = userDetailsService.getAllUserDetails();
			List<UserDetailsDTO> userDetailsDTOs = DtoConverter.convertToDtoList(userDetailsList, UserDetailsDTO.class);
			return ResponseEntity.ok(new ApiResponse<>(true, "UserDetails retrieved successfully", userDetailsDTOs));
		} catch (Exception e) {
			logger.error("Error retrieving all user details", e);
			return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getUserDetailsByToken() {
	    logger.info("Fetching user details for the authenticated user");

	    try {
	        // Get the current authenticated user from the token
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName(); // Get username (email) instead of user ID
	        logger.info("Authenticated username: {}", username);

	        // Fetch user details from the database using the username (email)
	        User existingUser = userService.findByUsername(username);
	        Long userId = existingUser.getId();
	        Optional<UserDetails> userDetails = userDetailsService.findByUserId(userId);
	        logger.info("User found: {}", userDetails);
	        logger.info("User ID: {}", userId);

	        // Convert UserDetails entity to UserDetailsDTO
	        UserDetailsDTO userDetailsDTO = DtoConverter.convertToDto(userDetails, UserDetailsDTO.class);
	        
	        // Fetch emergency contacts associated with user details
	        List<EmergencyContact> emergencyContacts = userDetails.get().getEmergencyContacts();
	        List<EmergencyContactDTO> emergencyContactDTOs = DtoConverter.convertToDtoList(emergencyContacts, EmergencyContactDTO.class);

	        userDetailsDTO.setEmergencyContacts(emergencyContactDTOs);

	        logger.info("Successfully retrieved user details for username: {}", username);

	        // Return success response with the UserDetailsDTO
	        return ResponseEntity.ok(new ApiResponse<>(true, "UserDetails retrieved successfully", userDetailsDTO));

	    } catch (UsernameNotFoundException e) {
	        logger.error("User not found for username in token: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("User not found.");

	    } catch (IllegalArgumentException e) {
	        logger.error("Invalid argument error: {}", e.getMessage(), e);
	        return ResponseEntity.badRequest()
	                .body("Invalid input provided. Please check the provided data and try again.");

	    } catch (Exception e) {
	        logger.error("Unexpected error occurred: {}", e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An unexpected error occurred. Please contact support if the issue persists.");
	    }
	}


	@PostMapping("/create")
	public ResponseEntity<ApiResponse<UserDetailsDTO>> createUserDetails(
	        @RequestBody UserDetailsDTO userDetailsDTO,
	        @RequestHeader("Authorization") String authorizationHeader) {

	    // Extract the access token from the Authorization header
	    String accessToken = authorizationHeader.substring("Bearer ".length());
	    String username;

	    try {
	        username = jwtUtil.extractUsername(accessToken); // Extract username from token
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiResponse<>(false, "Invalid access token", null));
	    }

	    if (!jwtUtil.validateAccessToken(accessToken, username)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(new ApiResponse<>(false, "Invalid access token", null));
	    }

	    try {
	        // Fetch the existing User from the database
	        User existingUser = userService.findByUsername(username);
	        
	        if (existingUser == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ApiResponse<>(false, "User not found", null));
	        }

	        // Convert DTO to Entity
	        UserDetails userDetails = DtoConverter.convertToEntity(userDetailsDTO, UserDetails.class);
	        userDetails.setUser(existingUser); // Set the persisted User to UserDetails
	        
	        
	        // Now save the UserDetails
	        UserDetails createdUserDetails = userDetailsService.createUserDetails(userDetails);
	        UserDetailsDTO createdUserDetailsDTO = DtoConverter.convertToDto(createdUserDetails, UserDetailsDTO.class);
	        
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(new ApiResponse<>(true, "UserDetails created successfully", createdUserDetailsDTO));
	        
	    } catch (Exception e) {
	        logger.error("Error creating user details", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse<>(false, "Internal Server Error", null));
	    }
	}


	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<UserDetailsDTO>> updateUserDetails(@PathVariable Long id,
			@RequestBody UserDetailsDTO userDetailsDTO) {
		try {
			UserDetails userDetails = DtoConverter.convertToEntity(userDetailsDTO, UserDetails.class);
			UserDetails updatedUserDetails = userDetailsService.updateUserDetails(id, userDetails);
			if (updatedUserDetails != null) {
				UserDetailsDTO updatedUserDetailsDTO = DtoConverter.convertToDto(updatedUserDetails,
						UserDetailsDTO.class);
				return ResponseEntity
						.ok(new ApiResponse<>(true, "UserDetails updated successfully", updatedUserDetailsDTO));
			} else {
				return ResponseEntity.status(404).body(new ApiResponse<>(false, "UserDetails not found", null));
			}
		} catch (Exception e) {
			logger.error("Error updating user details with id: {}", id, e);
			return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteUserDetails(@PathVariable Long id) {
		try {
			boolean deleted = userDetailsService.deleteUserDetails(id);
			if (deleted) {
				return ResponseEntity.ok(new ApiResponse<>(true, "UserDetails deleted successfully", null));
			} else {
				return ResponseEntity.status(404).body(new ApiResponse<>(false, "UserDetails not found", null));
			}
		} catch (Exception e) {
			logger.error("Error deleting user details with id: {}", id, e);
			return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
		}
	}
	
	@PutMapping("/profile-picture")
	public ResponseEntity<ApiResponse<String>> updateProfilePicture(@RequestParam("file") MultipartFile file) {
	    // Get authenticated user's username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		logger.info("entered into this");
		// Check if the file is not empty
		if (file.isEmpty()) {
		    return ResponseEntity.status(HttpStatus.ACCEPTED)
		            .body(new ApiResponse<>(false, "File is empty", null));
		}

		// Upload the file to S3 and get the URL
		String profilePictureUrl = s3Service.uploadFile(file);
		
		// Find the existing user
		User existingUser = userService.findByUsername(username);
		
		// Get the associated UserDetails
		UserDetails userDetails = userDetailsService.findByUserId(existingUser.getId()).orElseThrow(() -> 
		    new RuntimeException("User details not found for user: " + username)
		);

		// Update the user's profile picture via service method
		boolean updated = userDetailsService.updateProfilePicture(userDetails, profilePictureUrl);
		if (updated) {
		    return ResponseEntity.ok(new ApiResponse<>(true, "Profile picture updated successfully", profilePictureUrl));
		} else {
		    return ResponseEntity.status(HttpStatus.NOT_FOUND)
		            .body(new ApiResponse<>(false, "User not found", null));
		}
	}
}
