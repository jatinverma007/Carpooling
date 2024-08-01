package com.carpooling.CPUserService.CPUserService.serviceImpl;


import com.carpooling.CPUserService.CPUserService.entities.UsersTnc;
import com.carpooling.CPUserService.CPUserService.repositories.UsersTncDao;
import com.carpooling.CPUserService.CPUserService.services.UsersTncService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersTncServiceImpl implements UsersTncService {

    @Autowired
    private UsersTncDao usersTncRepository;

    @Override
    public List<UsersTnc> getAllUsersTnc() {
        return usersTncRepository.findAll();
    }

    @Override
    public Optional<UsersTnc> getUsersTncById(Long id) {
        return usersTncRepository.findById(id);
    }

    @Override
    public UsersTnc createUsersTnc(UsersTnc usersTnc) {
        return usersTncRepository.save(usersTnc);
    }

    @Override
    public UsersTnc updateUsersTnc(Long id, UsersTnc usersTncDetails) {
        return usersTncRepository.findById(id)
                .map(usersTnc -> {
                    usersTnc.setAccepted(usersTncDetails.isAccepted());
                    usersTnc.setAcceptedDate(usersTncDetails.getAcceptedDate());
                    return usersTncRepository.save(usersTnc);
                }).orElse(null);
    }

    @Override
    public void deleteUsersTnc(Long id) {
        usersTncRepository.deleteById(id);
    }
}
