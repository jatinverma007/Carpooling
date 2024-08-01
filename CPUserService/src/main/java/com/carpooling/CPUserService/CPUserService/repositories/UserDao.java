package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.User;

//User Dao
public interface UserDao extends JpaRepository<User, Long> {
}


