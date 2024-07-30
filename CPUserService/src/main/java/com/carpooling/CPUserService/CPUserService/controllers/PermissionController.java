package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.Permission;
import com.carpooling.CPUserService.CPUserService.repositories.PermissionRepository;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        return permission != null ? ResponseEntity.ok(permission) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionRepository.save(permission);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            permissionRepository.delete(permission);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}


