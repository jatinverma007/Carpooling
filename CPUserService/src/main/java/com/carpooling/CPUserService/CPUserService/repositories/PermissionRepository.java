package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.Permission;

//Permission Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
