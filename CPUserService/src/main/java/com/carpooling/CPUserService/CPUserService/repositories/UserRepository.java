package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.User;

//User Repository
public interface UserRepository extends JpaRepository<User, Long> {
}


