package com.carpooling.ums.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
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
import com.carpooling.ums.utils.Utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


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
    
    @Autowired
    private JavaMailSender mailSender;

    
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
            
            // Check if email is verified
            if (!user.isVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Please verify your email before logging in.");
            }

            // Convert User entity to UserDTO, which includes UserDetailsDTO, AddressDTO, and EmergencyContactDTO
            UserDTO userDTO = DtoConverter.convertToDto(user, UserDTO.class);
            userDTO.setConditionalVerificationToken(false, null);

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
    public ResponseEntity<?> signUp(@RequestBody UserDTO user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken); 
        user.setConditionalVerificationToken(true, verificationToken);
        logger.error("verification token: {}", verificationToken);

        try {
            // Save the user in the database
            ResponseEntity<?> response = userService.createUser(user);
            
            // Send verification email
            sendVerificationEmail(user, request);
            
            return response;
        } catch (Exception e) {
            logger.error("Error occurred while creating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A database error occurred. Please try again later.");
        }
    }






    private void sendVerificationEmail(UserDTO user, HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), ""); 
        String verificationToken = UUID.randomUUID().toString(); 
        String subject = "Please verify your registration";
        String senderName = "Jatin Verma";
        String mailContent = "<p>Dear " + user.getUsername() + ",</p>";
        mailContent += "<p>Please click the link below to verify your registration:</p>";
        String verifyURL = siteURL + "/verify?token=" + verificationToken; // Verification link with token
        mailContent += "<a href=\"" + verifyURL + "\">VERIFY YOUR EMAIL</a>";
        mailContent += "<p>Thank you,<br>The Team</p>";

        sendEmail(user.getUsername(), subject, mailContent);
    }
    
    private void sendEmail(String recipientEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("jatin.v199708@gmail.com", "Jatin Verma");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send email to {}: {}", recipientEmail, e.getMessage());
            e.printStackTrace(); // This will print the full stack trace to the console
            throw new RuntimeException("Failed to send email", e);
        }
    }


    
 

}
