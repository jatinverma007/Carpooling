package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.RolesPermissionAction;

//RolesPermissionAction Repository
public interface RolesPermissionActionRepository extends JpaRepository<RolesPermissionAction, Long> {
}
