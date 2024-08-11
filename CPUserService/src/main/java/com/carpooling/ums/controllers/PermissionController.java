package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carpooling.ums.dto.PermissionDTO;
import com.carpooling.ums.entities.Permission;
import com.carpooling.ums.services.PermissionService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private @Autowired PermissionService permissionService;
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> getAllPermissions() {
        try {
            List<Permission> permissions = permissionService.getAllPermissions();
            List<PermissionDTO> permissionDTOs = DtoConverter.convertToDtoList(permissions, PermissionDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Permissions retrieved successfully", permissionDTOs));
        } catch (Exception e) {
            logger.error("Error retrieving permissions", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDTO>> getPermissionById(@PathVariable Long id) {
        try {
            Permission permission = permissionService.getPermissionById(id);
            if (permission != null) {
                PermissionDTO permissionDTO = DtoConverter.convertToDto(permission, PermissionDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Permission retrieved successfully", permissionDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Permission not found", null));
            }
        } catch (Exception e) {
            logger.error("Error retrieving permission with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionDTO>> createPermission(@RequestBody PermissionDTO permissionDTO) {
        try {
            Permission permission = DtoConverter.convertToEntity(permissionDTO, Permission.class);
            Permission createdPermission = permissionService.createPermission(permission);
            PermissionDTO createdPermissionDTO = DtoConverter.convertToDto(createdPermission, PermissionDTO.class);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, "Permission created successfully", createdPermissionDTO));
        } catch (Exception e) {
            logger.error("Error creating permission", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Long id) {
        try {
            boolean deleted = permissionService.deletePermission(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Permission deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Permission not found", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting permission with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
