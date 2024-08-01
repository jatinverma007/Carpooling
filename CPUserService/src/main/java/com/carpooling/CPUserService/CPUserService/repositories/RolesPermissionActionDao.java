package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.RolesPermissionAction;

//RolesPermissionAction Dao
public interface RolesPermissionActionDao extends JpaRepository<RolesPermissionAction, Long> {
}
