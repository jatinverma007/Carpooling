package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;
import com.carpooling.ums.entities.Role;
import com.carpooling.ums.exceptions.RoleServiceException;
import com.carpooling.ums.repositories.RoleDao;
import com.carpooling.ums.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDao roleRepository;

    @Override
    public List<Role> getAllRoles() {
        try {
            return roleRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all roles", e);
            throw new RoleServiceException("Unable to retrieve roles. Please try again later.", e);
        }
    }

    @Override
    public Role getRoleById(Long id) {
        try {
            return roleRepository.findById(id).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching role with id: " + id, e);
            throw new RoleServiceException("Unable to retrieve role. Please try again later.", e);
        }
    }

    @Override
    public Role createRole(Role role) {
        try {
            return roleRepository.save(role);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating role", e);
            throw new RoleServiceException("Unable to create role. Please try again later.", e);
        }
    }

    @Override
    public boolean deleteRole(Long id) {
        try {
            Optional<Role> roleOptional = roleRepository.findById(id);
            if (roleOptional.isPresent()) {
                roleRepository.delete(roleOptional.get());
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting role with id: " + id, e);
            throw new RoleServiceException("Unable to delete role. Please try again later.", e);
        }
    }
}
