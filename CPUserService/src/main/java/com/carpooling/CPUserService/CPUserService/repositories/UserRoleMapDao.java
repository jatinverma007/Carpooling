package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.UserRoleMap;

//UserRoleMap Dao
public interface UserRoleMapDao extends JpaRepository<UserRoleMap, Long> {
}
