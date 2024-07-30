package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.UserDetails;
import com.carpooling.CPUserService.CPUserService.repositories.UserDetailsRepository;

import java.util.List;

@RestController
@RequestMapping("/api/userdetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @GetMapping
    public List<UserDetails> getAllUserDetails() {
        return userDetailsRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetails> getUserDetailsById(@PathVariable Long id) {
        UserDetails userDetails = userDetailsRepository.findById(id).orElse(null);
        return userDetails != null ? ResponseEntity.ok(userDetails) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public UserDetails createUserDetails(@RequestBody UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetails> updateUserDetails(@PathVariable Long id, @RequestBody UserDetails userDetailsDetails) {
        UserDetails userDetails = userDetailsRepository.findById(id).orElse(null);
        if (userDetails != null) {
            userDetails.setFirstname(userDetailsDetails.getFirstname());
            userDetails.setLastname(userDetailsDetails.getLastname());
            userDetails.setAddress(userDetailsDetails.getAddress());
            userDetails.setDob(userDetailsDetails.getDob());
            userDetails.setGender(userDetailsDetails.getGender());
            userDetails.setProfilePicture(userDetailsDetails.getProfilePicture());
            userDetails.setBio(userDetailsDetails.getBio());
            userDetails.setRegistrationDate(userDetailsDetails.getRegistrationDate());
            userDetails.setLastLoginDate(userDetailsDetails.getLastLoginDate());
            UserDetails updatedUserDetails = userDetailsRepository.save(userDetails);
            return ResponseEntity.ok(updatedUserDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable Long id) {
        UserDetails userDetails = userDetailsRepository.findById(id).orElse(null);
        if (userDetails != null) {
            userDetailsRepository.delete(userDetails);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

