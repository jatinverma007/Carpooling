package com.carpooling.ums.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.entities.AuthenticationResponse;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    ResponseEntity<?> createUser(UserDTO user);
    boolean deleteUser(Long id);
    Boolean verifyUserByToken(String token);    
    User findByUsername(String username);
}
