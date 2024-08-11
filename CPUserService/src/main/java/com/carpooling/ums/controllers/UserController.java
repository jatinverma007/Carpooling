package com.carpooling.ums.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.services.UserDetailsService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = DtoConverter.convertToDtoList(users, UserDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", userDTOs));
        } catch (Exception e) {
            logger.error("Error retrieving all users", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            return userService.getUserById(id)
                    .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", DtoConverter.convertToDto(user, UserDTO.class))))
                    .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null)));
        } catch (Exception e) {
            logger.error("Error retrieving user with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDetails userDetails = userDetailsService.getUserDetailsById(userDTO.getUserDetailsId())
                    .orElse(null);

            if (userDetails == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "UserDetails not found", null));
            }

            User user = DtoConverter.convertToEntity(userDTO, User.class);
            user.setUserDetails(userDetails);

            User createdUser = userService.createUser(user);
            UserDTO createdUserDTO = DtoConverter.convertToDto(createdUser, UserDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", createdUserDTO));
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting user with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
