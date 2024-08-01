package com.carpooling.CPUserService.CPUserService.serviceImpl;

import com.carpooling.CPUserService.CPUserService.entities.Permission;
import com.carpooling.CPUserService.CPUserService.repositories.PermissionDao;
import com.carpooling.CPUserService.CPUserService.services.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            permissionRepository.delete(permission);
        }
    }
}
