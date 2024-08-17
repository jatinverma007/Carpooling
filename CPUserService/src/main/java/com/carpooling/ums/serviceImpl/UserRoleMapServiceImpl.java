package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;
import com.carpooling.ums.entities.UserRoleMap;
import com.carpooling.ums.exceptions.UserRoleMapServiceException;
import com.carpooling.ums.repositories.UserRoleMapDao;
import com.carpooling.ums.services.UserRoleMapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleMapServiceImpl implements UserRoleMapService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleMapServiceImpl.class);

    @Autowired
    private UserRoleMapDao userRoleMapRepository;

    @Override
    public List<UserRoleMap> getAllUserRoleMaps() {
        try {
            return userRoleMapRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all user role maps", e);
            throw new UserRoleMapServiceException("Unable to retrieve user role maps. Please try again later.", e);
        }
    }

    @Override
    public Optional<UserRoleMap> getUserRoleMapById(Long id) {
        try {
            return userRoleMapRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching user role map with id: " + id, e);
            throw new UserRoleMapServiceException("Unable to retrieve user role map. Please try again later.", e);
        }
    }

    @Override
    public UserRoleMap createUserRoleMap(UserRoleMap userRoleMap) {
        try {
            return userRoleMapRepository.save(userRoleMap);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating user role map", e);
            throw new UserRoleMapServiceException("Unable to create user role map. Please try again later.", e);
        }
    }

    @Override
    public UserRoleMap updateUserRoleMap(Long id, UserRoleMap userRoleMapDetails) {
        try {
            return userRoleMapRepository.findById(id)
                .map(userRoleMap -> {
                    userRoleMap.setUser(userRoleMapDetails.getUser());
                    userRoleMap.setRole(userRoleMapDetails.getRole());
                    return userRoleMapRepository.save(userRoleMap);
                }).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error occurred while updating user role map with id: " + id, e);
            throw new UserRoleMapServiceException("Unable to update user role map. Please try again later.", e);
        }
    }

    @Override
    public boolean deleteUserRoleMap(Long id) {
        try {
            if (userRoleMapRepository.existsById(id)) {
                userRoleMapRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting user role map with id: " + id, e);
            throw new UserRoleMapServiceException("Unable to delete user role map. Please try again later.", e);
        }
    }
}
