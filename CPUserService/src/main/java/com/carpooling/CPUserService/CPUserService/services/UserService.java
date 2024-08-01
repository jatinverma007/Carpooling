package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.CPUserService.CPUserService.entities.User;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    void deleteUser(Long id);
}
