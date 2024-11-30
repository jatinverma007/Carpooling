package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carpooling.ums.dto.RoleDTO;
import com.carpooling.ums.entities.Role;
import com.carpooling.ums.services.RoleService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDTO>>> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            List<RoleDTO> roleDTOs = DtoConverter.convertToDtoList(roles, RoleDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Roles retrieved successfully", roleDTOs));
        } catch (Exception e) {
            logger.error("Error retrieving roles", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDTO>> getRoleById(@PathVariable Long id) {
        try {
            Role role = roleService.getRoleById(id);
            if (role != null) {
                RoleDTO roleDTO = DtoConverter.convertToDto(role, RoleDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Role retrieved successfully", roleDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Role not found", null));
            }
        } catch (Exception e) {
            logger.error("Error retrieving role with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            Role role = DtoConverter.convertToEntity(roleDTO, Role.class);
            Role createdRole = roleService.createRole(role);
            RoleDTO createdRoleDTO = DtoConverter.convertToDto(createdRole, RoleDTO.class);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, "Role created successfully", createdRoleDTO));
        } catch (Exception e) {
            logger.error("Error creating role", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        try {
            boolean deleted = roleService.deleteRole(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Role deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Role not found", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting role with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
