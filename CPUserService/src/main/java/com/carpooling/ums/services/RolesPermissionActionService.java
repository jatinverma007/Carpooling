package com.carpooling.ums.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.ums.entities.RolesPermissionAction;

public interface RolesPermissionActionService {

    List<RolesPermissionAction> getAllRolesPermissions();
    Optional<RolesPermissionAction> getRolesPermissionById(Long id);
    RolesPermissionAction createRolesPermissionAction(RolesPermissionAction rolesPermissionAction);
    boolean deleteRolesPermissionAction(Long id);
}
