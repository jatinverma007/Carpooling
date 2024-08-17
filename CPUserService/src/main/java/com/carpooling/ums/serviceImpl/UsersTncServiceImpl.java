package com.carpooling.ums.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;
import com.carpooling.ums.entities.UsersTnc;
import com.carpooling.ums.exceptions.UsersTncServiceException;
import com.carpooling.ums.repositories.UsersTncDao;
import com.carpooling.ums.services.UsersTncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class UsersTncServiceImpl implements UsersTncService {

    private static final Logger logger = LoggerFactory.getLogger(UsersTncServiceImpl.class);

    @Autowired
    private UsersTncDao usersTncRepository;

    @Override
    public List<UsersTnc> getAllUsersTnc() {
        try {
            return usersTncRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all UsersTnc", e);
            throw new UsersTncServiceException("Unable to retrieve UsersTnc. Please try again later.", e);
        }
    }

    @Override
    public Optional<UsersTnc> getUsersTncById(Long id) {
        try {
            return usersTncRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching UsersTnc with id: " + id, e);
            throw new UsersTncServiceException("Unable to retrieve UsersTnc. Please try again later.", e);
        }
    }

    @Override
    public UsersTnc createUsersTnc(UsersTnc usersTnc) {
        try {
            return usersTncRepository.save(usersTnc);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating UsersTnc", e);
            throw new UsersTncServiceException("Unable to create UsersTnc. Please try again later.", e);
        }
    }

    @Override
    public UsersTnc updateUsersTnc(Long id, UsersTnc usersTncDetails) {
        try {
            return usersTncRepository.findById(id)
                .map(usersTnc -> {
                    usersTnc.setAccepted(usersTncDetails.isAccepted());
                    usersTnc.setAcceptedDate(usersTncDetails.getAcceptedDate());
                    return usersTncRepository.save(usersTnc);
                }).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error occurred while updating UsersTnc with id: " + id, e);
            throw new UsersTncServiceException("Unable to update UsersTnc. Please try again later.", e);
        }
    }

    @Override
    public boolean deleteUsersTnc(Long id) {
        try {
            if (usersTncRepository.existsById(id)) {
                usersTncRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting UsersTnc with id: " + id, e);
            throw new UsersTncServiceException("Unable to delete UsersTnc. Please try again later.", e);
        }
    }
}
