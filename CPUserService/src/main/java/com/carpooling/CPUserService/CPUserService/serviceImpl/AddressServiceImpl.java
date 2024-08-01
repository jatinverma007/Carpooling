package com.carpooling.CPUserService.CPUserService.serviceImpl;

import com.carpooling.CPUserService.CPUserService.entities.Address;
import com.carpooling.CPUserService.CPUserService.repositories.AddressDao;
import com.carpooling.CPUserService.CPUserService.services.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressRepository;

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long id, Address addressDetails) {
        Address address = addressRepository.findById(id).orElse(null);
        if (address != null) {
            address.setStreet(addressDetails.getStreet());
            address.setCity(addressDetails.getCity());
            address.setState(addressDetails.getState());
            address.setZipCode(addressDetails.getZipCode());
            return addressRepository.save(address);
        }
        return null;
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
