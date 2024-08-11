package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carpooling.ums.dto.RolesPermissionActionDTO;
import com.carpooling.ums.entities.RolesPermissionAction;
import com.carpooling.ums.services.RolesPermissionActionService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/rolespermissions")
public class RolesPermissionActionController {

    @Autowired
    private RolesPermissionActionService rolesPermissionActionService;

    private static final Logger logger = LoggerFactory.getLogger(RolesPermissionActionController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolesPermissionActionDTO>>> getAllRolesPermissions() {
        try {
            List<RolesPermissionAction> rolesPermissions = rolesPermissionActionService.getAllRolesPermissions();
            List<RolesPermissionActionDTO> rolesPermissionsDTOs = DtoConverter.convertToDtoList(rolesPermissions, RolesPermissionActionDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Roles and permissions retrieved successfully", rolesPermissionsDTOs));
        } catch (Exception e) {
            logger.error("Error retrieving roles and permissions", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolesPermissionActionDTO>> getRolesPermissionById(@PathVariable Long id) {
        try {
            Optional<RolesPermissionAction> rolesPermissionOptional = rolesPermissionActionService.getRolesPermissionById(id);
            if (rolesPermissionOptional.isPresent()) {
                RolesPermissionActionDTO rolesPermissionDTO = DtoConverter.convertToDto(rolesPermissionOptional.get(), RolesPermissionActionDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Roles and permission retrieved successfully", rolesPermissionDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Roles and permission not found", null));
            }
        } catch (Exception e) {
            logger.error("Error retrieving roles and permission with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RolesPermissionActionDTO>> createRolesPermissionAction(@RequestBody RolesPermissionActionDTO rolesPermissionActionDTO) {
        try {
            RolesPermissionAction rolesPermissionAction = DtoConverter.convertToEntity(rolesPermissionActionDTO, RolesPermissionAction.class);
            RolesPermissionAction createdRolesPermissionAction = rolesPermissionActionService.createRolesPermissionAction(rolesPermissionAction);
            RolesPermissionActionDTO createdRolesPermissionActionDTO = DtoConverter.convertToDto(createdRolesPermissionAction, RolesPermissionActionDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Roles and permission created successfully", createdRolesPermissionActionDTO));
        } catch (Exception e) {
            logger.error("Error creating roles and permission", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRolesPermissionAction(@PathVariable Long id) {
        try {
            boolean deleted = rolesPermissionActionService.deleteRolesPermissionAction(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Roles and permission deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Roles and permission not found", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting roles and permission with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
