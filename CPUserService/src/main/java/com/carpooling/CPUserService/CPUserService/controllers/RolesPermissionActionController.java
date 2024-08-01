package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.RolesPermissionAction;
import com.carpooling.CPUserService.CPUserService.services.RolesPermissionActionService;

import java.util.List;

@RestController
@RequestMapping("/rolespermissions")
public class RolesPermissionActionController {

    @Autowired
    private RolesPermissionActionService rolesPermissionActionService;

    @GetMapping
    public List<RolesPermissionAction> getAllRolesPermissions() {
        return rolesPermissionActionService.getAllRolesPermissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesPermissionAction> getRolesPermissionById(@PathVariable Long id) {
        return rolesPermissionActionService.getRolesPermissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RolesPermissionAction createRolesPermissionAction(@RequestBody RolesPermissionAction rolesPermissionAction) {
        return rolesPermissionActionService.createRolesPermissionAction(rolesPermissionAction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRolesPermissionAction(@PathVariable Long id) {
        rolesPermissionActionService.deleteRolesPermissionAction(id);
        return ResponseEntity.noContent().build();
    }
}
