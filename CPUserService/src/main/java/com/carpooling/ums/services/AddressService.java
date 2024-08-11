package com.carpooling.ums.services;

import java.util.List;

import com.carpooling.ums.dto.AddressDTO;
import com.carpooling.ums.entities.Address;

public interface AddressService {

    List<Address> getAllAddresses();
    Address getAddressById(Long id);
    Address createAddress(Address address);
    Address updateAddress(Long id, AddressDTO addressDTO);
    boolean deleteAddress(Long id);

}
