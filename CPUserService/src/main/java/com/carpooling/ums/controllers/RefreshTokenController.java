package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.carpooling.ums.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("/refresh-token")
public class RefreshTokenController {

    private final JwtUtil jwtUtil;

    @Autowired
    public RefreshTokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<?> refreshAuthenticationToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is missing.");
        }
        
        try {
            // Check if the token is expired
            if (!jwtUtil.isTokenExpired(refreshToken)) {
                String username = jwtUtil.extractUsername(refreshToken);
                String newAccessToken = jwtUtil.generateAccessToken(username);
                String newRefreshToken = jwtUtil.generateRefreshToken(username);

                Map<String, Object> response = new HashMap<>();
                response.put("accessToken", newAccessToken);
                response.put("refreshToken", newRefreshToken);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token has expired.");
            }
        } catch (ExpiredJwtException e) {
            // Handle the expired token error
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token has expired.");
        } catch (JwtException e) {
            // Handle other JWT-related exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
        } catch (Exception e) {
            // Handle general server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the token.");
        }
    }


}
