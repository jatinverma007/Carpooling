package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.UserRoleMap;
import com.carpooling.CPUserService.CPUserService.repositories.UserRoleMapRepository;

import java.util.List;

@RestController
@RequestMapping("/api/userroles")
public class UserRoleMapController {

    @Autowired
    private UserRoleMapRepository userRoleMapRepository;

    @GetMapping
    public List<UserRoleMap> getAllUserRoleMaps() {
        return userRoleMapRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRoleMap> getUserRoleMapById(@PathVariable Long id) {
        UserRoleMap userRoleMap = userRoleMapRepository.findById(id).orElse(null);
        return userRoleMap != null ? ResponseEntity.ok(userRoleMap) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public UserRoleMap createUserRoleMap(@RequestBody UserRoleMap userRoleMap) {
        return userRoleMapRepository.save(userRoleMap);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRoleMap> updateUserRoleMap(@PathVariable Long id, @RequestBody UserRoleMap userRoleMapDetails) {
        UserRoleMap userRoleMap = userRoleMapRepository.findById(id).orElse(null);
        if (userRoleMap != null) {
            userRoleMap.setUser(userRoleMapDetails.getUser());
            userRoleMap.setRole(userRoleMapDetails.getRole());
            UserRoleMap updatedUserRoleMap = userRoleMapRepository.save(userRoleMap);
            return ResponseEntity.ok(updatedUserRoleMap);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRoleMap(@PathVariable Long id) {
        UserRoleMap userRoleMap = userRoleMapRepository.findById(id).orElse(null);
        if (userRoleMap != null) {
            userRoleMapRepository.delete(userRoleMap);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

