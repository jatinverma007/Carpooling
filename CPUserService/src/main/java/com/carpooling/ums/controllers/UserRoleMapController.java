package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carpooling.ums.dto.UserRoleMapDTO;
import com.carpooling.ums.entities.UserRoleMap;
import com.carpooling.ums.services.UserRoleMapService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/userroles")
public class UserRoleMapController {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleMapController.class);

    @Autowired
    private UserRoleMapService userRoleMapService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserRoleMapDTO>>> getAllUserRoleMaps() {
        try {
            List<UserRoleMap> userRoleMaps = userRoleMapService.getAllUserRoleMaps();
            List<UserRoleMapDTO> userRoleMapDTOs = DtoConverter.convertToDtoList(userRoleMaps, UserRoleMapDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "User role maps fetched successfully", userRoleMapDTOs));
        } catch (Exception e) {
            logger.error("Error fetching all user role maps", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRoleMapDTO>> getUserRoleMapById(@PathVariable Long id) {
        try {
            return userRoleMapService.getUserRoleMapById(id)
                    .map(userRoleMap -> {
                        UserRoleMapDTO userRoleMapDTO = DtoConverter.convertToDto(userRoleMap, UserRoleMapDTO.class);
                        return ResponseEntity.ok(new ApiResponse<>(true, "User role map fetched successfully", userRoleMapDTO));
                    })
                    .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "User role map not found", null)));
        } catch (Exception e) {
            logger.error("Error fetching user role map with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserRoleMapDTO>> createUserRoleMap(@RequestBody UserRoleMapDTO userRoleMapDTO) {
        try {
            UserRoleMap userRoleMap = DtoConverter.convertToEntity(userRoleMapDTO, UserRoleMap.class);
            UserRoleMap createdUserRoleMap = userRoleMapService.createUserRoleMap(userRoleMap);
            UserRoleMapDTO createdUserRoleMapDTO = DtoConverter.convertToDto(createdUserRoleMap, UserRoleMapDTO.class);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, "User role map created successfully", createdUserRoleMapDTO));
        } catch (Exception e) {
            logger.error("Error creating user role map", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRoleMapDTO>> updateUserRoleMap(@PathVariable Long id, @RequestBody UserRoleMapDTO userRoleMapDTO) {
        try {
            UserRoleMap userRoleMap = DtoConverter.convertToEntity(userRoleMapDTO, UserRoleMap.class);
            UserRoleMap updatedUserRoleMap = userRoleMapService.updateUserRoleMap(id, userRoleMap);
            if (updatedUserRoleMap != null) {
                UserRoleMapDTO updatedUserRoleMapDTO = DtoConverter.convertToDto(updatedUserRoleMap, UserRoleMapDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "User role map updated successfully", updatedUserRoleMapDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User role map not found", null));
            }
        } catch (Exception e) {
            logger.error("Error updating user role map with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserRoleMap(@PathVariable Long id) {
        try {
            boolean deleted = userRoleMapService.deleteUserRoleMap(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "User role map deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User role map not found", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting user role map with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
