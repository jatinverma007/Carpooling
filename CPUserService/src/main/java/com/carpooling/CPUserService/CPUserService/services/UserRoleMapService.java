package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.CPUserService.CPUserService.entities.UserRoleMap;

public interface UserRoleMapService {

    List<UserRoleMap> getAllUserRoleMaps();
    Optional<UserRoleMap> getUserRoleMapById(Long id);
    UserRoleMap createUserRoleMap(UserRoleMap userRoleMap);
    UserRoleMap updateUserRoleMap(Long id, UserRoleMap userRoleMapDetails);
    void deleteUserRoleMap(Long id);
}
