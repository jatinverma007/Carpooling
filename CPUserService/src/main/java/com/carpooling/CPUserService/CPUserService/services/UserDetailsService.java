package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.CPUserService.CPUserService.entities.UserDetails;

public interface UserDetailsService {

    List<UserDetails> getAllUserDetails();
    Optional<UserDetails> getUserDetailsById(Long id);
    UserDetails createUserDetails(UserDetails userDetails);
    UserDetails updateUserDetails(Long id, UserDetails userDetailsDetails);
    void deleteUserDetails(Long id);
}
