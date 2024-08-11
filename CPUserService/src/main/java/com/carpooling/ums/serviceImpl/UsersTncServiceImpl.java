package com.carpooling.ums.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpooling.ums.entities.UsersTnc;
import com.carpooling.ums.repositories.UsersTncDao;
import com.carpooling.ums.services.UsersTncService;

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
    public boolean deleteUsersTnc(Long id) {
        if (usersTncRepository.existsById(id)) {
            usersTncRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
