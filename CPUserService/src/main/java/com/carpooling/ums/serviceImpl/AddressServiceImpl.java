package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpooling.ums.dto.AddressDTO;
import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.repositories.AddressDao;
import com.carpooling.ums.services.AddressService;
import com.carpooling.ums.services.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressRepository;
    
    @Autowired
    private UserService userService;

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
    public Address updateAddress(Long id, AddressDTO addressDTO) {
        Address address = addressRepository.findById(id).orElse(null);
        if (address != null) {
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setZipCode(addressDTO.getZipCode());

            Optional<User> userOptional = userService.getUserById(addressDTO.getUserId());
//            if (userOptional.isPresent()) {
//                address.setUser(userOptional.get());
//            }

            return addressRepository.save(address);
        }
        return null;
    }

    @Override
    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
