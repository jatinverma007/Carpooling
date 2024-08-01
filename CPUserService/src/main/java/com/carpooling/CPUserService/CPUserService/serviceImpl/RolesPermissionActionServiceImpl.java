package com.carpooling.CPUserService.CPUserService.serviceImpl;


import com.carpooling.CPUserService.CPUserService.entities.RolesPermissionAction;
import com.carpooling.CPUserService.CPUserService.repositories.RolesPermissionActionDao;
import com.carpooling.CPUserService.CPUserService.services.RolesPermissionActionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void deleteRolesPermissionAction(Long id) {
        rolesPermissionActionRepository.deleteById(id);
    }
}

