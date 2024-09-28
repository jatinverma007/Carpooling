package com.carpooling.ums.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carpooling.ums.entities.UserDetails;

//UserDetails Dao
public interface UserDetailsDao extends JpaRepository<UserDetails, Long> {
	
	Optional<UserDetails> findByUserId(Long userId);
    
}


