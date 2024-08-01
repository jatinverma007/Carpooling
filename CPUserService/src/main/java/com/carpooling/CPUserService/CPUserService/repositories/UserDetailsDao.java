package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.UserDetails;

//UserDetails Dao
public interface UserDetailsDao extends JpaRepository<UserDetails, Long> {
}
