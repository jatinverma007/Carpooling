package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.EmergencyContact;

//EmergencyContact Dao
public interface EmergencyContactDao extends JpaRepository<EmergencyContact, Long> {
}
