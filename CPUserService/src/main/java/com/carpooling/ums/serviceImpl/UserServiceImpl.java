package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.carpooling.ums.dto.AddressDTO;
import com.carpooling.ums.dto.EmergencyContactDTO;
import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.dto.UserDetailsDTO;
import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.AuthenticationRequest;
import com.carpooling.ums.entities.AuthenticationResponse;
import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.entities.ErrorResponse;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.exceptions.UserServiceException;
import com.carpooling.ums.repositories.UserDao;
import com.carpooling.ums.repositories.UserDetailsDao;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.DtoConverter;
import com.carpooling.ums.utils.JwtUtil;

import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userRepository;

	@Autowired
	private UserDetailsDao userDetailsRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public List<User> getAllUsers() {
		try {
			return userRepository.findAll();
		} catch (DataAccessException e) {
			logger.error("Error occurred while fetching all users", e);
			throw new UserServiceException("Unable to retrieve users. Please try again later.", e);
		}
	}

	@Override
	public Optional<User> getUserById(Long id) {
		try {
			return userRepository.findById(id);
		} catch (DataAccessException e) {
			logger.error("Error occurred while fetching user with id: " + id, e);
			throw new UserServiceException("Unable to retrieve user. Please try again later.", e);
		}
	}

	@Override
	public ResponseEntity<?> createUser(UserDTO userDTO) {
		try {
			// Convert UserDTO to User entity
			User user = convertToUser(userDTO);

			// Save User entity
			User savedUser = userRepository.save(user);

			// Convert saved user entity to UserDTO
			UserDTO savedUserDTO = new UserDTO();
			savedUserDTO.setUsername(savedUser.getUsername());
			savedUserDTO.setPassword(savedUser.getPassword());
			savedUserDTO.setStatus(savedUser.getStatus());
			savedUserDTO.setRoles(savedUser.getRoles());

			// Generate JWT token
			String jwtToken = jwtUtil.generateToken(savedUser.getUsername());

			// Return AuthenticationResponse with JWT token and UserDTO
			return ResponseEntity.ok(new AuthenticationResponse(jwtToken, savedUserDTO));

		} catch (BadCredentialsException e) {
            logger.error("Authentication failed: Bad credentials for username: {}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password.");

        } catch (DataAccessException e) {
            logger.error("Database error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("A database error occurred. Please try again later.");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body("Invalid input provided. Please check the provided data and try again.");

        } catch (ConstraintViolationException e) {
            logger.error("Constraint violation error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body("Data constraint violation occurred. Please ensure all required fields are provided correctly.");

        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please contact support if the issue persists.");
        }
	}

	private User convertToUser(UserDTO userDTO) {
		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setStatus(userDTO.getStatus());
		user.setRoles(userDTO.getRoles());
		return user;
	}

	@Override
	public boolean deleteUser(Long id) {
		try {
			if (userRepository.existsById(id)) {
				userRepository.deleteById(id);
				return true;
			}
			return false;
		} catch (DataAccessException e) {
			logger.error("Error occurred while deleting user with id: " + id, e);
			throw new UserServiceException("Unable to delete user. Please try again later.", e);
		}
	}
}
