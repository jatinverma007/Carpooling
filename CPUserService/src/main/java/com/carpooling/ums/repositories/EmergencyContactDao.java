package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carpooling.ums.entities.EmergencyContact;

//EmergencyContact Dao
@Repository
public interface EmergencyContactDao extends JpaRepository<EmergencyContact, Long> {
}
