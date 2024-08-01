package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.CPUserService.CPUserService.entities.RolesPermissionAction;

public interface RolesPermissionActionService {

    List<RolesPermissionAction> getAllRolesPermissions();
    Optional<RolesPermissionAction> getRolesPermissionById(Long id);
    RolesPermissionAction createRolesPermissionAction(RolesPermissionAction rolesPermissionAction);
    void deleteRolesPermissionAction(Long id);
}
