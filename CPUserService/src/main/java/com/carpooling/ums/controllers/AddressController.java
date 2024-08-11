package com.carpooling.ums.controllers;

import com.carpooling.ums.dto.AddressDTO;
import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.services.AddressService;
import com.carpooling.ums.services.UserDetailsService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/address")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAllAddresses() {
        try {
            List<Address> addresses = addressService.getAllAddresses();
            List<AddressDTO> addressDTOs = DtoConverter.convertToDtoList(addresses, AddressDTO.class);
            return ResponseEntity.ok(new ApiResponse<>(true, "Addresses fetched successfully", addressDTOs));
        } catch (Exception e) {
            logger.error("Error fetching all addresses", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDTO>> getAddressById(@PathVariable Long id) {
        try {
            Address address = addressService.getAddressById(id);
            if (address != null) {
                AddressDTO addressDTO = DtoConverter.convertToDto(address, AddressDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Address fetched successfully", addressDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Address not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching address by id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDTO>> createAddress(@RequestBody AddressDTO addressDTO) {
        try {
            Optional<UserDetails> userDetailsOptional = userDetailsService.getUserDetailsById(addressDTO.getUserId());
            if (!userDetailsOptional.isPresent()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "User not found", null));
            }

            Address address = DtoConverter.convertToEntity(addressDTO, Address.class);
            address.setUserDetails(userDetailsOptional.get());

            Address createdAddress = addressService.createAddress(address);
            AddressDTO createdAddressDTO = DtoConverter.convertToDto(createdAddress, AddressDTO.class);

            return ResponseEntity.ok(new ApiResponse<>(true, "Address created successfully", createdAddressDTO));
        } catch (Exception e) {
            logger.error("Error creating address", e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDTO>> updateAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO) {
        try {
            Address updatedAddress = addressService.updateAddress(id, addressDTO);
            if (updatedAddress != null) {
                AddressDTO updatedAddressDTO = DtoConverter.convertToDto(updatedAddress, AddressDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Address updated successfully", updatedAddressDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Address not found", null));
            }
        } catch (Exception e) {
            logger.error("Error updating address with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id) {
        try {
            boolean deleted = addressService.deleteAddress(id);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Address deleted successfully", null));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Address not found", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting address with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

}
