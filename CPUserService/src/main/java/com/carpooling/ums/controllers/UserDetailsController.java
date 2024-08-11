package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carpooling.ums.dto.UserDetailsDTO;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.services.UserDetailsService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/userdetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDetailsDTO>>> getAllUserDetails() {
        try {
            List<UserDetails> userDetailsList = userDetailsService.getAllUserDetails();
            List<UserDetailsDTO> userDetailsDTOs = DtoConverter.convertToDtoList(userDetailsList, UserDetailsDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "UserDetails retrieved successfully", userDetailsDTOs));
        } catch (Exception e) {
            logger.error("Error retrieving all user details", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetailsDTO>> getUserDetailsById(@PathVariable Long id) {
        try {
            return userDetailsService.getUserDetailsById(id)
                    .map(userDetails -> {
                        UserDetailsDTO userDetailsDTO = DtoConverter.convertToDto(userDetails, UserDetailsDTO.class);
                        return ResponseEntity.ok(new ApiResponse<>(true, "UserDetails retrieved successfully", userDetailsDTO));
                    })
                    .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "UserDetails not found", null)));
        } catch (Exception e) {
            logger.error("Error retrieving user details with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDetailsDTO>> createUserDetails(@RequestBody UserDetailsDTO userDetailsDTO) {
        try {
            UserDetails userDetails = DtoConverter.convertToEntity(userDetailsDTO, UserDetails.class);
            UserDetails createdUserDetails = userDetailsService.createUserDetails(userDetails);
            UserDetailsDTO createdUserDetailsDTO = DtoConverter.convertToDto(createdUserDetails, UserDetailsDTO.class);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, "UserDetails created successfully", createdUserDetailsDTO));
        } catch (Exception e) {
            logger.error("Error creating user details", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetailsDTO>> updateUserDetails(@PathVariable Long id, @RequestBody UserDetailsDTO userDetailsDTO) {
        try {
            UserDetails userDetails = DtoConverter.convertToEntity(userDetailsDTO, UserDetails.class);
            UserDetails updatedUserDetails = userDetailsService.updateUserDetails(id, userDetails);
            if (updatedUserDetails != null) {
                UserDetailsDTO updatedUserDetailsDTO = DtoConverter.convertToDto(updatedUserDetails, UserDetailsDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "UserDetails updated successfully", updatedUserDetailsDTO));
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
}
