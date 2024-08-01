package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.Address;

//Address Dao
public interface AddressDao extends JpaRepository<Address, Long> {
}
