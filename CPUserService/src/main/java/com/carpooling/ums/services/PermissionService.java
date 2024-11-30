package com.carpooling.ums.services;

import java.util.List;

import com.carpooling.ums.entities.Permission;

public interface PermissionService {

    List<Permission> getAllPermissions();
    Permission getPermissionById(Long id);
    Permission createPermission(Permission permission);
    boolean deletePermission(Long id);
}
