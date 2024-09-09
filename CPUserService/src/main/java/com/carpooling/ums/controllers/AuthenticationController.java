package com.carpooling.ums.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.carpooling.ums.entities.User;
import com.carpooling.ums.filters.JwtRequestFilter;
import com.carpooling.ums.services.MyUserDetailsService;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.DtoConverter;
import com.carpooling.ums.utils.JwtUtil;

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
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        logger.info("createAuthenticationToken");
        try {
            logger.info("createAuthenticationToken : username : {} : password : {}",authenticationRequest.getUsername(),authenticationRequest.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            logger.info("createAuthenticationToken : userDetails : {}", userDetails);
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            // Convert UserDetails to UserDetailsDTO
            UserDetailsDTO userDetailsDTO = DtoConverter.convertToDto(userDetails, UserDetailsDTO.class);
            
            return new AuthenticationResponse(jwt, userDetailsDTO);
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }
    }




    @PostMapping("/signup")
    public User signUp(@RequestBody UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }
    
 

}
