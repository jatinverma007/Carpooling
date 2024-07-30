package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.RolesPermissionAction;
import com.carpooling.CPUserService.CPUserService.repositories.RolesPermissionActionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/rolespermissions")
public class RolesPermissionActionController {

    @Autowired
    private RolesPermissionActionRepository rolesPermissionActionRepository;

    @GetMapping
    public List<RolesPermissionAction> getAllRolesPermissions() {
        return rolesPermissionActionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesPermissionAction> getRolesPermissionById(@PathVariable Long id) {
        RolesPermissionAction rolesPermissionAction = rolesPermissionActionRepository.findById(id).orElse(null);
        return rolesPermissionAction != null ? ResponseEntity.ok(rolesPermissionAction) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public RolesPermissionAction createRolesPermissionAction(@RequestBody RolesPermissionAction rolesPermissionAction) {
        return rolesPermissionActionRepository.save(rolesPermissionAction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRolesPermissionAction(@PathVariable Long id) {
        RolesPermissionAction rolesPermissionAction = rolesPermissionActionRepository.findById(id).orElse(null);
        if (rolesPermissionAction != null) {
            rolesPermissionActionRepository.delete(rolesPermissionAction);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
