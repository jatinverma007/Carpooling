package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.Role;

//UserDetails Dao
public interface RoleDao extends JpaRepository<Role, Long> {
}
