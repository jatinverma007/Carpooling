package com.carpooling.ums.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.dto.UserDetailsDTO;
import com.carpooling.ums.entities.AuthenticationRequest;
import com.carpooling.ums.entities.AuthenticationResponse;
import com.carpooling.ums.entities.ErrorResponse;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.filters.JwtRequestFilter;
import com.carpooling.ums.repositories.UserDao;
import com.carpooling.ums.services.MyUserDetailsService;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.DtoConverter;
import com.carpooling.ums.utils.JwtUtil;

import jakarta.validation.ConstraintViolationException;

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
    private  PasswordEncoder passwordEncoder;

    @Autowired
	private UserDao userRepository;

    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("createAuthenticationToken");
        try {
            logger.info("createAuthenticationToken : username : {} : password : {}", authenticationRequest.getUsername(), authenticationRequest.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            
            // Load UserDetails by username
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            logger.info("createAuthenticationToken : userDetails : {}", userDetails);
            
            // Generate JWT token
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            // Fetch the full User entity from the database
            User user = Optional.ofNullable(userRepository.findByUsername(authenticationRequest.getUsername()))
                    .orElseThrow(() -> new Exception("User not found"));

            // Convert User entity to UserDTO, which includes UserDetailsDTO, AddressDTO, and EmergencyContactDTO
            UserDTO userDTO = DtoConverter.convertToDto(user, UserDTO.class);

            // Return AuthenticationResponse with JWT token and UserDTO
            return ResponseEntity.ok(new AuthenticationResponse(jwt, userDTO));
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed: Bad credentials for username: {}", authenticationRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password.");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body("Invalid input provided. Please check the provided data and try again.");

        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please contact support if the issue persists.");
        }
    }






    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }
    
 

}
