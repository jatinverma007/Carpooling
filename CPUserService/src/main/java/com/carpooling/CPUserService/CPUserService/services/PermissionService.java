package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;

import com.carpooling.CPUserService.CPUserService.entities.Permission;

public interface PermissionService {

    List<Permission> getAllPermissions();
    Permission getPermissionById(Long id);
    Permission createPermission(Permission permission);
    void deletePermission(Long id);
}
