package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.ums.dto.UsersTncDTO;
import com.carpooling.ums.entities.UsersTnc;
import com.carpooling.ums.services.UsersTncService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/userstnc")
public class UsersTncController {

    private static final Logger logger = LoggerFactory.getLogger(UsersTncController.class);

    @Autowired
    private UsersTncService usersTncService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsersTncDTO>>> getAllUsersTnc() {
        try {
            List<UsersTnc> usersTncList = usersTncService.getAllUsersTnc();
            List<UsersTncDTO> usersTncDTOs = DtoConverter.convertToDtoList(usersTncList, UsersTncDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Users TNCs fetched successfully", usersTncDTOs));
        } catch (Exception e) {
            logger.error("Error fetching all users TNCs", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsersTncDTO>> getUsersTncById(@PathVariable Long id) {
        try {
            return usersTncService.getUsersTncById(id)
                    .map(usersTnc -> {
                        UsersTncDTO usersTncDTO = DtoConverter.convertToDto(usersTnc, UsersTncDTO.class);
                        return ResponseEntity.ok(new ApiResponse<>(true, "Users TNC fetched successfully", usersTncDTO));
                    })
                    .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Users TNC not found", null)));
        } catch (Exception e) {
            logger.error("Error fetching users TNC with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsersTncDTO>> createUsersTnc(@RequestBody UsersTncDTO usersTncDTO) {
        try {
            UsersTnc usersTnc = DtoConverter.convertToEntity(usersTncDTO, UsersTnc.class);
            UsersTnc createdUsersTnc = usersTncService.createUsersTnc(usersTnc);
            UsersTncDTO createdUsersTncDTO = DtoConverter.convertToDto(createdUsersTnc, UsersTncDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Users TNC created successfully", createdUsersTncDTO));
        } catch (Exception e) {
            logger.error("Error creating users TNC", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsersTncDTO>> updateUsersTnc(@PathVariable Long id, @RequestBody UsersTncDTO usersTncDTO) {
        try {
            UsersTnc usersTnc = DtoConverter.convertToEntity(usersTncDTO, UsersTnc.class);
            UsersTnc updatedUsersTnc = usersTncService.updateUsersTnc(id, usersTnc);
            return updatedUsersTnc != null
                    ? ResponseEntity.ok(new ApiResponse<>(true, "Users TNC updated successfully", DtoConverter.convertToDto(updatedUsersTnc, UsersTncDTO.class)))
                    : ResponseEntity.status(404).body(new ApiResponse<>(false, "Users TNC not found", null));
        } catch (Exception e) {
            logger.error("Error updating users TNC with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUsersTnc(@PathVariable Long id) {
        try {
            usersTncService.deleteUsersTnc(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting users TNC with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
