package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.carpooling.ums.dto.EmergencyContactDTO;
import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.services.EmergencyContactService;
import com.carpooling.ums.services.UserDetailsService;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emergencycontact")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;
    
    private static final Logger logger = LoggerFactory.getLogger(EmergencyContactController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmergencyContactDTO>>> getAllEmergencyContacts() {
        try {
            // Fetch authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            logger.info("Authenticated username: {}", username);

            // Find the user by username
            User existingUser = userService.findByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
            }

            // Fetch UserDetails using the User ID
            Long userId = existingUser.getId();
            Optional<UserDetails> userDetailsOptional = userDetailsService.findByUserId(userId);
            if (userDetailsOptional.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User details not found", null));
            }

            // Get emergency contacts linked to the user's details
            List<EmergencyContact> emergencyContacts = emergencyContactService.getEmergencyContactsByUserDetails(userDetailsOptional.get());

            if (emergencyContacts.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "No emergency contacts found for the user", null));
            }

            // Convert EmergencyContact entities to DTOs
            List<EmergencyContactDTO> emergencyContactDTOs = DtoConverter.convertToDtoList(emergencyContacts, EmergencyContactDTO.class);

            return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contacts retrieved successfully", emergencyContactDTOs));
        } catch (Exception e) {
            logger.error("Error retrieving emergency contacts", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<EmergencyContactDTO>> createEmergencyContact(@RequestBody EmergencyContactDTO emergencyContactDTO) {
        try {
            // Extract username from token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            logger.info("Authenticated username: {}", username);

            // Fetch user from the database using username
            User existingUser = userService.findByUsername(username);
            UserDetails userDetails = userDetailsService.findByUserId(existingUser.getId()).orElseThrow(() -> 
                new RuntimeException("User details not found for user: " + username)
            );

            // Save the emergency contact for the user
            EmergencyContact savedEmergencyContact = emergencyContactService.saveEmergencyContact(emergencyContactDTO, userDetails);

            // Convert to DTO to return in response
            EmergencyContactDTO savedEmergencyContactDTO = DtoConverter.convertToDto(savedEmergencyContact, EmergencyContactDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contact created successfully", savedEmergencyContactDTO));

        } catch (Exception e) {
            logger.error("Error creating emergency contact", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<EmergencyContactDTO>> getEmergencyContactById(@PathVariable Long id) {
//        try {
//            EmergencyContact emergencyContact = emergencyContactService.getEmergencyContactById(id);
//            if (emergencyContact != null) {
//                EmergencyContactDTO emergencyContactDTO = DtoConverter.convertToDto(emergencyContact, EmergencyContactDTO.class);
//                return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contact retrieved successfully", emergencyContactDTO));
//            } else {
//                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Emergency contact not found", null));
//            }
//        } catch (Exception e) {
//            logger.error("Error retrieving emergency contact with id: {}", id, e);
//            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
//        }
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmergencyContactDTO>> updateEmergencyContact(@PathVariable Long id, @RequestBody EmergencyContactDTO emergencyContactDTO) {
        try {
            EmergencyContact emergencyContact = DtoConverter.convertToEntity(emergencyContactDTO, EmergencyContact.class);
            EmergencyContact updatedEmergencyContact = emergencyContactService.updateEmergencyContact(id, emergencyContact);
            if (updatedEmergencyContact != null) {
                EmergencyContactDTO updatedEmergencyContactDTO = DtoConverter.convertToDto(updatedEmergencyContact, EmergencyContactDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contact updated successfully", updatedEmergencyContactDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Emergency contact not found", null));
            }
        } catch (Exception e) {
            logger.error("Error updating emergency contact with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmergencyContact(@PathVariable Long id) {
        try {
            // Extract username from token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            logger.info("Authenticated username: {}", username);

            // Fetch user from the database using username
            User existingUser = userService.findByUsername(username);
            UserDetails userDetails = userDetailsService.findByUserId(existingUser.getId()).orElseThrow(() -> 
                new RuntimeException("User details not found for user: " + username)
            );

            // Check if emergency contact exists and delete it
            Optional<EmergencyContact> emergencyContactOpt = emergencyContactService.findById(id);

            if (!emergencyContactOpt.isPresent() || !emergencyContactOpt.get().getUserDetails().equals(userDetails)) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "Emergency contact not found or does not belong to the user.", null));
            }

            emergencyContactService.deleteEmergencyContact(id);

            return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contact deleted successfully", null));

        } catch (Exception e) {
            logger.error("Error deleting emergency contact", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

}
