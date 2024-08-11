package com.carpooling.ums.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpooling.ums.entities.RolesPermissionAction;
import com.carpooling.ums.repositories.RolesPermissionActionDao;
import com.carpooling.ums.services.RolesPermissionActionService;

import java.util.List;
import java.util.Optional;

@Service
public class RolesPermissionActionServiceImpl implements RolesPermissionActionService {

    @Autowired
    private RolesPermissionActionDao rolesPermissionActionRepository;

    @Override
    public List<RolesPermissionAction> getAllRolesPermissions() {
        return rolesPermissionActionRepository.findAll();
    }

    @Override
    public Optional<RolesPermissionAction> getRolesPermissionById(Long id) {
        return rolesPermissionActionRepository.findById(id);
    }

    @Override
    public RolesPermissionAction createRolesPermissionAction(RolesPermissionAction rolesPermissionAction) {
        return rolesPermissionActionRepository.save(rolesPermissionAction);
    }

    @Override
    public boolean deleteRolesPermissionAction(Long id) {
        if (rolesPermissionActionRepository.existsById(id)) {
            rolesPermissionActionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

