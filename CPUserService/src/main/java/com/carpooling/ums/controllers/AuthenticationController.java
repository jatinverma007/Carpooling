package com.carpooling.ums.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.entities.AuthenticationRequest;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.repositories.UserDao;
import com.carpooling.ums.services.MyUserDetailsService;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.DtoConverter;
import com.carpooling.ums.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String NMS_SERVICE_URL = "http://localhost:8084/nms/api/notifications/send-email";

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("Attempting to authenticate user: {}", authenticationRequest.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            String jwt = jwtUtil.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            User user = Optional.ofNullable(userRepository.findByUsername(authenticationRequest.getUsername()))
                    .orElseThrow(() -> new Exception("User not found"));

            if (!user.isVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Please verify your email before logging in.");
            }

            user.setToken(jwt);
            userRepository.save(user);

            UserDTO userDTO = DtoConverter.convertToDto(user, UserDTO.class);
            userDTO.setConditionalVerificationToken(false, null);

            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", jwt);
            response.put("refreshToken", refreshToken);
            response.put("user", userDTO);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed: Bad credentials for username: {}", authenticationRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password.");
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please contact support.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDTO user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setConditionalVerificationToken(true, verificationToken);
        logger.info("Generated verification token: {}", verificationToken);

        try {
            ResponseEntity<?> response = userService.createUser(user);
            sendVerificationEmailNMS(user, request);
            return response;
        } catch (Exception e) {
            logger.error("Error occurred while creating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A database error occurred. Please try again later.");
        }
    }

    private void sendVerificationEmailNMS(UserDTO user, HttpServletRequest request) {
        // Existing code to construct verification link
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
        String verificationToken = user.getVerificationToken();
        String verifyURL = siteURL + "/verify?token=" + verificationToken;

        Map<String, String> emailDetails = new HashMap<>();
        emailDetails.put("to", user.getUsername());
        emailDetails.put("subject", "Please verify your registration");
        emailDetails.put("body", "<p>Dear " + user.getUsername() + ",</p>"
                + "<p>Please click the link below to verify your registration:</p>"
                + "<a href=\"" + verifyURL + "\">VERIFY YOUR EMAIL</a>"
                + "<p>Thank you,<br>The Team</p>");

        // Log email details
        System.out.println("Email Details: " + emailDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(emailDetails, headers);

        try {
            // Send the email using RestTemplate
            ResponseEntity<String> response = restTemplate.postForEntity(NMS_SERVICE_URL, requestEntity, String.class);
            System.out.println("Verification email sent via NMS: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send verification email via NMS: " + e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }



}
