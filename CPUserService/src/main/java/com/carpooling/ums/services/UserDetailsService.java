package com.carpooling.ums.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.ums.entities.UserDetails;

public interface UserDetailsService {

    List<UserDetails> getAllUserDetails();
    Optional<UserDetails> getUserDetailsById(Long id);
    UserDetails createUserDetails(UserDetails userDetails);
    UserDetails updateUserDetails(Long id, UserDetails userDetailsDetails);
    boolean deleteUserDetails(Long id);
}
