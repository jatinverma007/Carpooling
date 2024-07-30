package com.carpooling.CPUserService.CPUserService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.CPUserService.CPUserService.entities.Address;

//Address Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
