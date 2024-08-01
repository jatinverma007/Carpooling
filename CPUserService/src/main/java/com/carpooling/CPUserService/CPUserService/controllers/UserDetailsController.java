package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.UserDetails;
import com.carpooling.CPUserService.CPUserService.services.UserDetailsService;

import java.util.List;

@RestController
@RequestMapping("/userdetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public List<UserDetails> getAllUserDetails() {
        return userDetailsService.getAllUserDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetails> getUserDetailsById(@PathVariable Long id) {
        return userDetailsService.getUserDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserDetails createUserDetails(@RequestBody UserDetails userDetails) {
        return userDetailsService.createUserDetails(userDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetails> updateUserDetails(@PathVariable Long id, @RequestBody UserDetails userDetailsDetails) {
        UserDetails updatedUserDetails = userDetailsService.updateUserDetails(id, userDetailsDetails);
        return updatedUserDetails != null ? ResponseEntity.ok(updatedUserDetails) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable Long id) {
        userDetailsService.deleteUserDetails(id);
        return ResponseEntity.noContent().build();
    }
}

