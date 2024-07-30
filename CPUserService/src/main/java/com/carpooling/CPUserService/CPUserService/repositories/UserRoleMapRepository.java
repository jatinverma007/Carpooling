package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.UserRoleMap;

//UserRoleMap Repository
public interface UserRoleMapRepository extends JpaRepository<UserRoleMap, Long> {
}
