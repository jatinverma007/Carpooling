package com.carpooling.ums.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.ums.entities.UserRoleMap;

public interface UserRoleMapService {

    List<UserRoleMap> getAllUserRoleMaps();
    Optional<UserRoleMap> getUserRoleMapById(Long id);
    UserRoleMap createUserRoleMap(UserRoleMap userRoleMap);
    UserRoleMap updateUserRoleMap(Long id, UserRoleMap userRoleMapDetails);
    boolean deleteUserRoleMap(Long id);
}
