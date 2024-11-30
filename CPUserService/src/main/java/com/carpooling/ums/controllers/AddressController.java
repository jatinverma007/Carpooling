package com.carpooling.ums.controllers;

import com.carpooling.ums.dto.AddressDTO;
import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.services.AddressService;
import com.carpooling.ums.services.UserDetailsService;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.ApiResponse;
import com.carpooling.ums.utils.DtoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAllAddresses() {
        try {
            // Fetch authenticated user's username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            logger.info("Authenticated username: {}", username);

            // Fetch user from the database using username
            User existingUser = userService.findByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
            }

            // Fetch UserDetails using the User ID
            Long userId = existingUser.getId();
            Optional<UserDetails> userDetailsOptional = userDetailsService.findByUserId(userId);
            if (userDetailsOptional.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User details not found", null));
            }

            // Get all addresses linked to the user's details
            List<Address> addresses = addressService.getAddressesByUserDetails(userDetailsOptional.get());
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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            logger.info("Authenticated username: {}", username);

            User existingUser = userService.findByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
            }

            UserDetails userDetails = userDetailsService.findByUserId(existingUser.getId()).orElseThrow(() -> 
                new RuntimeException("User details not found for user: " + username)
            );

            // Fetch the address by ID and check if it belongs to the user
            Address address = addressService.getAddressById(id);
            if (address != null && address.getUserDetails().equals(userDetails)) {
                AddressDTO addressDTO = DtoConverter.convertToDto(address, AddressDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Address fetched successfully", addressDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Address not found or does not belong to the user", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching address by id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AddressDTO>> createAddress(@RequestBody AddressDTO addressDTO) {
        try {
            // Extract username from token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            logger.info("Authenticated username: {}", username);

            // Fetch user from the database using username
            User existingUser = userService.findByUsername(username);
            UserDetails userDetails = userDetailsService.findByUserId(existingUser.getId()).orElseThrow(() -> 
                new RuntimeException("User details not found for user: " + username)
            );

            Address address = DtoConverter.convertToEntity(addressDTO, Address.class);
            address.setUserDetails(userDetails);

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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            logger.info("Authenticated username: {}", username);

            User existingUser = userService.findByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
            }

            UserDetails userDetails = userDetailsService.findByUserId(existingUser.getId()).orElseThrow(() -> 
                new RuntimeException("User details not found for user: " + username)
            );

            // Fetch the address and check if it belongs to the user
            Address existingAddress = addressService.getAddressById(id);
            if (existingAddress != null && existingAddress.getUserDetails().equals(userDetails)) {
                Address updatedAddress = addressService.updateAddress(id, addressDTO);
                AddressDTO updatedAddressDTO = DtoConverter.convertToDto(updatedAddress, AddressDTO.class);
                return ResponseEntity.ok(new ApiResponse<>(true, "Address updated successfully", updatedAddressDTO));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Address not found or does not belong to the user", null));
            }
        } catch (Exception e) {
            logger.error("Error updating address with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            logger.info("Authenticated username: {}", username);

            User existingUser = userService.findByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found", null));
            }

            UserDetails userDetails = userDetailsService.findByUserId(existingUser.getId()).orElseThrow(() -> 
                new RuntimeException("User details not found for user: " + username)
            );

            // Check if the address exists and belongs to the user
            Address address = addressService.getAddressById(id);
            if (address != null && address.getUserDetails().equals(userDetails)) {
                boolean deleted = addressService.deleteAddress(id);
                if (deleted) {
                    return ResponseEntity.ok(new ApiResponse<>(true, "Address deleted successfully", null));
                } else {
                    return ResponseEntity.status(404).body(new ApiResponse<>(false, "Address not found", null));
                }
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Address not found or does not belong to the user", null));
            }
        } catch (Exception e) {
            logger.error("Error deleting address with id: {}", id, e);
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Internal Server Error", null));
        }
    }
}
