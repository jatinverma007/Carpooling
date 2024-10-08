package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;

import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.exceptions.UserDetailsServiceException;
import com.carpooling.ums.repositories.UserDetailsDao;
import com.carpooling.ums.services.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserDetailsDao userDetailsRepository;

    @Override
    public List<UserDetails> getAllUserDetails() {
        try {
            return userDetailsRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all user details", e);
            throw new UserDetailsServiceException("Unable to retrieve user details. Please try again later.", e);
        }
    }

    @Override
    public Optional<UserDetails> getUserDetailsById(Long id) {
        try {
            return userDetailsRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching user details with id: " + id, e);
            throw new UserDetailsServiceException("Unable to retrieve user details. Please try again later.", e);
        }
    }

    @Override
    public UserDetails createUserDetails(UserDetails userDetails) {
        try {
            return userDetailsRepository.save(userDetails);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating user details", e);
            throw new UserDetailsServiceException("Unable to create user details. Please try again later.", e);
        }
    }

    @Override
    public UserDetails updateUserDetails(Long id, UserDetails userDetailsDetails) {
        try {
            return userDetailsRepository.findById(id)
                .map(userDetails -> {
                    userDetails.setFirstname(userDetailsDetails.getFirstname());
                    userDetails.setLastname(userDetailsDetails.getLastname());
                    userDetails.setDob(userDetailsDetails.getDob());
                    userDetails.setGender(userDetailsDetails.getGender());
                    userDetails.setProfilePicture(userDetailsDetails.getProfilePicture());
                    userDetails.setBio(userDetailsDetails.getBio());
//                    userDetails.setRegistrationDate(userDetailsDetails.getRegistrationDate());
//                    userDetails.setLastLoginDate(userDetailsDetails.getLastLoginDate());
                    return userDetailsRepository.save(userDetails);
                }).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error occurred while updating user details with id: " + id, e);
            throw new UserDetailsServiceException("Unable to update user details. Please try again later.", e);
        }
    }

    @Override
    public boolean deleteUserDetails(Long id) {
        try {
            if (userDetailsRepository.existsById(id)) {
                userDetailsRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting user details with id: " + id, e);
            throw new UserDetailsServiceException("Unable to delete user details. Please try again later.", e);
        }
    }
    
    @Override
    public Optional<UserDetails> findByUserId(Long userId) {
        logger.info("Fetching UserDetails for userId: {}", userId);
        Optional<UserDetails> userDetails = userDetailsRepository.findByUserId(userId);
        
        if (userDetails.isPresent()) {
            logger.info("UserDetails found for userId: {}", userId);
        } else {
            logger.warn("No UserDetails found for userId: {}", userId);
        }
        
        return userDetails;
    }

    @Override
    public boolean updateProfilePicture(UserDetails userDetails, String profilePictureUrl) {
        try {
            // Set the new profile picture URL
            userDetails.setProfilePicture(profilePictureUrl);

            // Save the updated UserDetails entity to the database
            userDetailsRepository.save(userDetails);
            return true; // Return true if the update was successful
        } catch (Exception e) {
            // Handle exception (log it, etc.)
            return false; // Return false if there was an error during the update
        }
    }
    
    
}
