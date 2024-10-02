package com.carpooling.ums.controllers;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carpooling.ums.entities.User;
import com.carpooling.ums.repositories.UserDao;
import com.carpooling.ums.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userRepository;


    @PostMapping("/user")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract the token after "Bearer "

            try {
                // Extract username from the token
                String username = jwtUtil.extractUsername(token);

                // Fetch the user by username
                User user = userRepository.findByUsername(username);
                if (user != null) {
                    // Remove the token by setting it to null
                    user.setToken(null);
                    userRepository.save(user); // Update the user record in the database
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
                }

                return ResponseEntity.ok("Logged out successfully. Token is invalidated.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout.");
            }

        } else {
            return ResponseEntity.badRequest().body("Authorization header is missing or invalid.");
        }
    }

}
