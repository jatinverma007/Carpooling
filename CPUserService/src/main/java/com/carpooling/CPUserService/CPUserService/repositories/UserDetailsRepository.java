package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.UserDetails;

//UserDetails Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
}
