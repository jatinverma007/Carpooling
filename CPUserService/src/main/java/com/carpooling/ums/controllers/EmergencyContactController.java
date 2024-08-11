package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carpooling.ums.dto.EmergencyContactDTO;
import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.services.EmergencyContactService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/emergencycontact")
public class EmergencyContactController {

    private @Autowired EmergencyContactService emergencyContactService;
    private static final Logger logger = LoggerFactory.getLogger(EmergencyContactController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmergencyContactDTO>>> getAllEmergencyContacts() {
        try {
            List<EmergencyContact> emergencyContacts = emergencyContactService.getAllEmergencyContacts();
            List<EmergencyContactDTO> emergencyContactDTOs = DtoConverter.convertToDtoList(emergencyContacts, EmergencyContactDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contacts retrieved successfully", emergencyContactDTOs));
        } catch (Exception e) {
            logger.error("Error retrieving emergency contacts", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmergencyContactDTO>> getEmergencyContactById(@PathVariable Long id) {
        try {
            EmergencyContact emergencyContact = emergencyContactService.getEmergencyContactById(id);
            if (emergencyContact != null) {
                EmergencyContactDTO emergencyContactDTO = DtoConverter.convertToDto(emergencyContact, EmergencyContactDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contact retrieved successfully", emergencyContactDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Emergency contact not found", null));
            }
        } catch (Exception e) {
            logger.error("Error retrieving emergency contact with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmergencyContactDTO>> createEmergencyContact(@RequestBody EmergencyContactDTO emergencyContactDTO) {
        try {
            EmergencyContact emergencyContact = DtoConverter.convertToEntity(emergencyContactDTO, EmergencyContact.class);
            EmergencyContact createdEmergencyContact = emergencyContactService.createEmergencyContact(emergencyContact);
            EmergencyContactDTO createdEmergencyContactDTO = DtoConverter.convertToDto(createdEmergencyContact, EmergencyContactDTO.class);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, "Emergency contact created successfully", createdEmergencyContactDTO));
        } catch (Exception e) {
            logger.error("Error creating emergency contact", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmergencyContact(@PathVariable Long id) {
        try {
            boolean deleted = emergencyContactService.deleteEmergencyContact(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Emergency contact deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Emergency contact not found", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting emergency contact with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
