package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;

import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.exceptions.UserServiceException;
import com.carpooling.ums.repositories.UserDao;
import com.carpooling.ums.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userRepository;

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
    public User createUser(UserDTO user) {
        try {
        	User userDet = new User();
        	userDet.setPassword(user.getPassword());
        	userDet.setRoles(user.getRoles());
        	userDet.setStatus(user.getStatus());
        	userDet.setUsername(user.getUsername());
            return userRepository.save(userDet);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating user", e);
            throw new UserServiceException("Unable to create user. Please try again later.", e);
        }
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
