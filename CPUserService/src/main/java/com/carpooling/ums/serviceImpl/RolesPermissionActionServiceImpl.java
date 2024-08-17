package com.carpooling.ums.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;
import com.carpooling.ums.entities.RolesPermissionAction;
import com.carpooling.ums.exceptions.RolesPermissionActionServiceException;
import com.carpooling.ums.repositories.RolesPermissionActionDao;
import com.carpooling.ums.services.RolesPermissionActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class RolesPermissionActionServiceImpl implements RolesPermissionActionService {

    private static final Logger logger = LoggerFactory.getLogger(RolesPermissionActionServiceImpl.class);

    @Autowired
    private RolesPermissionActionDao rolesPermissionActionRepository;

    @Override
    public List<RolesPermissionAction> getAllRolesPermissions() {
        try {
            return rolesPermissionActionRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all roles permissions", e);
            throw new RolesPermissionActionServiceException("Unable to retrieve roles permissions. Please try again later.", e);
        }
    }

    @Override
    public Optional<RolesPermissionAction> getRolesPermissionById(Long id) {
        try {
            return rolesPermissionActionRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching roles permission with id: " + id, e);
            throw new RolesPermissionActionServiceException("Unable to retrieve roles permission. Please try again later.", e);
        }
    }

    @Override
    public RolesPermissionAction createRolesPermissionAction(RolesPermissionAction rolesPermissionAction) {
        try {
            return rolesPermissionActionRepository.save(rolesPermissionAction);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating roles permission action", e);
            throw new RolesPermissionActionServiceException("Unable to create roles permission action. Please try again later.", e);
        }
    }

    @Override
    public boolean deleteRolesPermissionAction(Long id) {
        try {
            if (rolesPermissionActionRepository.existsById(id)) {
                rolesPermissionActionRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting roles permission action with id: " + id, e);
            throw new RolesPermissionActionServiceException("Unable to delete roles permission action. Please try again later.", e);
        }
    }
}
