package com.carpooling.ums.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.UserDetails;

//Address Dao
public interface AddressDao extends JpaRepository<Address, Long> {

	List<Address> findByUserDetails(UserDetails userDetails);
}
