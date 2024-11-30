package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.Role;

//UserDetails Dao
public interface RoleDao extends JpaRepository<Role, Long> {
}
