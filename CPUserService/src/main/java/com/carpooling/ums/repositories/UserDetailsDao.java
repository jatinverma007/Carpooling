package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.UserDetails;

//UserDetails Dao
public interface UserDetailsDao extends JpaRepository<UserDetails, Long> {
}
