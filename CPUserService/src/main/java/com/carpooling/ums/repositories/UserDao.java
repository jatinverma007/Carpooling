package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.User;

//User Dao
public interface UserDao extends JpaRepository<User, Long> {
}


