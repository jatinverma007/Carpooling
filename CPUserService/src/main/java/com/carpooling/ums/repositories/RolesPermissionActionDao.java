package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.RolesPermissionAction;

//RolesPermissionAction Dao
public interface RolesPermissionActionDao extends JpaRepository<RolesPermissionAction, Long> {
}
