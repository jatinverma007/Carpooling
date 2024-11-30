package com.carpooling.ums.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.ums.entities.UsersTnc;

public interface UsersTncService {
	
    List<UsersTnc> getAllUsersTnc();
    Optional<UsersTnc> getUsersTncById(Long id);
    UsersTnc createUsersTnc(UsersTnc usersTnc);
    UsersTnc updateUsersTnc(Long id, UsersTnc usersTncDetails);
    boolean deleteUsersTnc(Long id);
}
