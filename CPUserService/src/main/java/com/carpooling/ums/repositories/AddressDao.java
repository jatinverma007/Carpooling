package com.carpooling.ums.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.Address;

//Address Dao
public interface AddressDao extends JpaRepository<Address, Long> {
}
