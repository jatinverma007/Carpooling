package com.carpooling.ums.services;

import java.util.List;

import com.carpooling.ums.entities.Role;

public interface RoleService {

    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role createRole(Role role);
    boolean deleteRole(Long id);
}
