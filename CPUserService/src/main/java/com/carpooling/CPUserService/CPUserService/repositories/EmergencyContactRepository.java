package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.EmergencyContact;

//EmergencyContact Repository
public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
}
