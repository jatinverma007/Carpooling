package com.carpooling.CPUserService.CPUserService.serviceImpl;

import com.carpooling.CPUserService.CPUserService.entities.UserDetails;
import com.carpooling.CPUserService.CPUserService.repositories.UserDetailsDao;
import com.carpooling.CPUserService.CPUserService.services.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                    userDetails.setAddress(userDetailsDetails.getAddress());
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
    public void deleteUserDetails(Long id) {
        userDetailsRepository.deleteById(id);
    }
}

