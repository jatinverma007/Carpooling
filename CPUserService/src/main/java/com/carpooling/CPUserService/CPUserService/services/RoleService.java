package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;

import com.carpooling.CPUserService.CPUserService.entities.Role;

public interface RoleService {

    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role createRole(Role role);
    void deleteRole(Long id);
}
