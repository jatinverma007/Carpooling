package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.UsersTnc;
import com.carpooling.CPUserService.CPUserService.services.UsersTncService;

import java.util.List;

@RestController
@RequestMapping("/userstnc")
public class UsersTncController {

    @Autowired
    private UsersTncService usersTncService;

    @GetMapping
    public List<UsersTnc> getAllUsersTnc() {
        return usersTncService.getAllUsersTnc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersTnc> getUsersTncById(@PathVariable Long id) {
        return usersTncService.getUsersTncById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsersTnc createUsersTnc(@RequestBody UsersTnc usersTnc) {
        return usersTncService.createUsersTnc(usersTnc);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersTnc> updateUsersTnc(@PathVariable Long id, @RequestBody UsersTnc usersTncDetails) {
        UsersTnc updatedUsersTnc = usersTncService.updateUsersTnc(id, usersTncDetails);
        return updatedUsersTnc != null ? ResponseEntity.ok(updatedUsersTnc) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsersTnc(@PathVariable Long id) {
        usersTncService.deleteUsersTnc(id);
        return ResponseEntity.noContent().build();
    }
}