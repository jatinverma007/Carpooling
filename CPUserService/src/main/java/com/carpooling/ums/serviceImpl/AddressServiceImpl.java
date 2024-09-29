package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpooling.ums.dto.AddressDTO;
import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.exceptions.AddressServiceException;
import com.carpooling.ums.repositories.AddressDao;
import com.carpooling.ums.services.AddressService;

import java.util.List;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;


@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressDao addressRepository;

    @Override
    public List<Address> getAllAddresses() {
        try {
            return addressRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all addresses", e);
            throw new AddressServiceException("Unable to retrieve addresses. Please try again later.", e);
        }
    }

    @Override
    public Address getAddressById(Long id) {
        try {
            return addressRepository.findById(id).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching address with id: " + id, e);
            throw new AddressServiceException("Unable to retrieve address. Please try again later.", e);
        }
    }

    @Override
    public Address createAddress(Address address) {
        try {
            return addressRepository.save(address);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating address", e);
            throw new AddressServiceException("Unable to create address. Please try again later.", e);
        }
    }

    @Override
    public Address updateAddress(Long id, AddressDTO addressDTO) {
        try {
            Address address = addressRepository.findById(id).orElse(null);
            if (address != null) {
                address.setStreet(addressDTO.getStreet());
                address.setCity(addressDTO.getCity());
                address.setState(addressDTO.getState());
                address.setZipCode(addressDTO.getZipCode());

                // If the user information is needed, you can uncomment and use the following block.
                // Optional<User> userOptional = userService.getUserById(addressDTO.getUserId());
                // if (userOptional.isPresent()) {
                //     address.setUser(userOptional.get());
                // }

                return addressRepository.save(address);
            }
            return null;
        } catch (DataAccessException e) {
            logger.error("Error occurred while updating address with id: " + id, e);
            throw new AddressServiceException("Unable to update address. Please try again later.", e);
        }
    }

    @Override
    public boolean deleteAddress(Long id) {
        try {
            if (addressRepository.existsById(id)) {
                addressRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting address with id: " + id, e);
            throw new AddressServiceException("Unable to delete address. Please try again later.", e);
        }
    }

	@Override
	public List<Address> getAddressesByUserDetails(UserDetails userDetails) {
        return addressRepository.findByUserDetails(userDetails);
	}
}
