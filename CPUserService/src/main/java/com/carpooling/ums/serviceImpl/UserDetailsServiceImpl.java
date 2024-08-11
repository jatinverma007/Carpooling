package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.repositories.UserDetailsDao;
import com.carpooling.ums.services.UserDetailsService;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDetailsDao userDetailsRepository;

    @Override
    public List<UserDetails> getAllUserDetails() {
        return userDetailsRepository.findAll();
    }

    @Override
    public Optional<UserDetails> getUserDetailsById(Long id) {
        return userDetailsRepository.findById(id);
    }

    @Override
    public UserDetails createUserDetails(UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }

    @Override
    public UserDetails updateUserDetails(Long id, UserDetails userDetailsDetails) {
        return userDetailsRepository.findById(id)
                .map(userDetails -> {
                    userDetails.setFirstname(userDetailsDetails.getFirstname());
                    userDetails.setLastname(userDetailsDetails.getLastname());
//                    userDetails.setAddress(userDetailsDetails.getAddress());
                    userDetails.setDob(userDetailsDetails.getDob());
                    userDetails.setGender(userDetailsDetails.getGender());
                    userDetails.setProfilePicture(userDetailsDetails.getProfilePicture());
                    userDetails.setBio(userDetailsDetails.getBio());
                    userDetails.setRegistrationDate(userDetailsDetails.getRegistrationDate());
                    userDetails.setLastLoginDate(userDetailsDetails.getLastLoginDate());
                    return userDetailsRepository.save(userDetails);
                }).orElse(null);
    }

    @Override
    public boolean deleteUserDetails(Long id) {
        if (userDetailsRepository.existsById(id)) {
            userDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

