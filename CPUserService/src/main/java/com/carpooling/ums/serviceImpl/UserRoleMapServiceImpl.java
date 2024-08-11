package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpooling.ums.entities.UserRoleMap;
import com.carpooling.ums.repositories.UserRoleMapDao;
import com.carpooling.ums.services.UserRoleMapService;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleMapServiceImpl implements UserRoleMapService {

    @Autowired
    private UserRoleMapDao userRoleMapRepository;

    @Override
    public List<UserRoleMap> getAllUserRoleMaps() {
        return userRoleMapRepository.findAll();
    }

    @Override
    public Optional<UserRoleMap> getUserRoleMapById(Long id) {
        return userRoleMapRepository.findById(id);
    }

    @Override
    public UserRoleMap createUserRoleMap(UserRoleMap userRoleMap) {
        return userRoleMapRepository.save(userRoleMap);
    }

    @Override
    public UserRoleMap updateUserRoleMap(Long id, UserRoleMap userRoleMapDetails) {
        return userRoleMapRepository.findById(id)
                .map(userRoleMap -> {
                    userRoleMap.setUser(userRoleMapDetails.getUser());
                    userRoleMap.setRole(userRoleMapDetails.getRole());
                    return userRoleMapRepository.save(userRoleMap);
                }).orElse(null);
    }

    @Override
    public boolean deleteUserRoleMap(Long id) {
        if (userRoleMapRepository.existsById(id)) {
            userRoleMapRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

