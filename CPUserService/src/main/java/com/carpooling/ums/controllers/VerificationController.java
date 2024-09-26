package com.carpooling.ums.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carpooling.ums.dto.VerificationTokenRequestDTO;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.services.UserService;

@RestController
@RequestMapping("/verify")
public class VerificationController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    @Autowired
    private UserService userService; // Assuming you have a UserService for database operations

    @PostMapping("/user")
    public ResponseEntity<String> verifyEmail(@RequestBody VerificationTokenRequestDTO tokenRequest) {
        String token = tokenRequest.getVerificationToken(); // Get the verification token from the request body

        boolean isVerified = userService.verifyUserByToken(token);
        if (isVerified) {
            return ResponseEntity.ok("Your email has been verified successfully!");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired token.");
        }
    }

}