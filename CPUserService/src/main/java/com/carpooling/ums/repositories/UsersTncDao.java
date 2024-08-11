package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.UsersTnc;

//UsersTnc Dao
public interface UsersTncDao extends JpaRepository<UsersTnc, Long> {
}
