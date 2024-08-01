package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;

import com.carpooling.CPUserService.CPUserService.entities.Address;

public interface AddressService {

    List<Address> getAllAddresses();
    Address getAddressById(Long id);
    Address createAddress(Address address);
    Address updateAddress(Long id, Address addressDetails);
    void deleteAddress(Long id);

}
