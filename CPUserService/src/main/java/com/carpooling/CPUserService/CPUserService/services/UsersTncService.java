package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.CPUserService.CPUserService.entities.UsersTnc;

public interface UsersTncService {
	
    List<UsersTnc> getAllUsersTnc();
    Optional<UsersTnc> getUsersTncById(Long id);
    UsersTnc createUsersTnc(UsersTnc usersTnc);
    UsersTnc updateUsersTnc(Long id, UsersTnc usersTncDetails);
    void deleteUsersTnc(Long id);
}
