package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.UsersTnc;

//UsersTnc Repository
public interface UsersTncRepository extends JpaRepository<UsersTnc, Long> {
}
