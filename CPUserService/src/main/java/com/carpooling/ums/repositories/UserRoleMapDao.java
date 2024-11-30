package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.UserRoleMap;

//UserRoleMap Dao
public interface UserRoleMapDao extends JpaRepository<UserRoleMap, Long> {
}
