package com.carpooling.ums.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.entities.UserDetails;

//EmergencyContact Dao
@Repository
public interface EmergencyContactDao extends JpaRepository<EmergencyContact, Long> {

	List<EmergencyContact> findByUserDetails(UserDetails userDetails);
}
