package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;
import com.carpooling.ums.entities.Permission;
import com.carpooling.ums.exceptions.PermissionServiceException;
import com.carpooling.ums.repositories.PermissionDao;
import com.carpooling.ums.services.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private PermissionDao permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        try {
            return permissionRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all permissions", e);
            throw new PermissionServiceException("Unable to retrieve permissions. Please try again later.", e);
        }
    }

    @Override
    public Permission getPermissionById(Long id) {
        try {
            return permissionRepository.findById(id).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching permission with id: " + id, e);
            throw new PermissionServiceException("Unable to retrieve permission. Please try again later.", e);
        }
    }

    @Override
    public Permission createPermission(Permission permission) {
        try {
            return permissionRepository.save(permission);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating permission", e);
            throw new PermissionServiceException("Unable to create permission. Please try again later.", e);
        }
    }

    @Override
    public boolean deletePermission(Long id) {
        try {
            Optional<Permission> permissionOptional = permissionRepository.findById(id);
            if (permissionOptional.isPresent()) {
                permissionRepository.delete(permissionOptional.get());
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting permission with id: " + id, e);
            throw new PermissionServiceException("Unable to delete permission. Please try again later.", e);
        }
    }
}
