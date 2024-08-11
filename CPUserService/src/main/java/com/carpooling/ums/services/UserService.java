package com.carpooling.ums.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.ums.entities.User;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    boolean deleteUser(Long id);
}
