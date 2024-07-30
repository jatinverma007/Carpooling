package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.Role;

//UserDetails Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
