package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.UserRoleMap;
import com.carpooling.CPUserService.CPUserService.services.UserRoleMapService;

import java.util.List;

@RestController
@RequestMapping("/userroles")
public class UserRoleMapController {

    @Autowired
    private UserRoleMapService userRoleMapService;

    @GetMapping
    public List<UserRoleMap> getAllUserRoleMaps() {
        return userRoleMapService.getAllUserRoleMaps();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleMap> getUserRoleMapById(@PathVariable Long id) {
        return userRoleMapService.getUserRoleMapById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserRoleMap createUserRoleMap(@RequestBody UserRoleMap userRoleMap) {
        return userRoleMapService.createUserRoleMap(userRoleMap);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleMap> updateUserRoleMap(@PathVariable Long id, @RequestBody UserRoleMap userRoleMapDetails) {
        UserRoleMap updatedUserRoleMap = userRoleMapService.updateUserRoleMap(id, userRoleMapDetails);
        return updatedUserRoleMap != null ? ResponseEntity.ok(updatedUserRoleMap) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRoleMap(@PathVariable Long id) {
        userRoleMapService.deleteUserRoleMap(id);
        return ResponseEntity.noContent().build();
    }
}

