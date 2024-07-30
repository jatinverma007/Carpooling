package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.UsersTnc;
import com.carpooling.CPUserService.CPUserService.repositories.UsersTncRepository;

import java.util.List;

@RestController
@RequestMapping("/api/userstnc")
public class UsersTncController {

    @Autowired
    private UsersTncRepository usersTncRepository;

    @GetMapping
    public List<UsersTnc> getAllUsersTnc() {
        return usersTncRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersTnc> getUsersTncById(@PathVariable Long id) {
        UsersTnc usersTnc = usersTncRepository.findById(id).orElse(null);
        return usersTnc != null ? ResponseEntity.ok(usersTnc) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public UsersTnc createUsersTnc(@RequestBody UsersTnc usersTnc) {
        return usersTncRepository.save(usersTnc);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsersTnc(@PathVariable Long id) {
        UsersTnc usersTnc = usersTncRepository.findById(id).orElse(null);
        if (usersTnc != null) {
            usersTncRepository.delete(usersTnc);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
