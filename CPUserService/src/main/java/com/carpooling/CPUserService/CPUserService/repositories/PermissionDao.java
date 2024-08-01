package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.Permission;

//Permission Dao
public interface PermissionDao extends JpaRepository<Permission, Long> {
}
