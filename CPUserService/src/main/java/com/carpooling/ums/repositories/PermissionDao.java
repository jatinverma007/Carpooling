package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.Permission;

//Permission Dao
public interface PermissionDao extends JpaRepository<Permission, Long> {
}
