package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.carpooling.ums.dto.AddressDTO;
import com.carpooling.ums.dto.EmergencyContactDTO;
import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.dto.UserDetailsDTO;
import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.AuthenticationRequest;
import com.carpooling.ums.entities.AuthenticationResponse;
import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.entities.ErrorResponse;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.exceptions.UserServiceException;
import com.carpooling.ums.repositories.UserDao;
import com.carpooling.ums.repositories.UserDetailsDao;
import com.carpooling.ums.services.UserService;
import com.carpooling.ums.utils.DtoConverter;
import com.carpooling.ums.utils.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userRepository;

	@Autowired
	private UserDetailsDao userDetailsRepository;
	
    @Autowired
    private JwtUtil jwtUtil;

	@Override
	public List<User> getAllUsers() {
		try {
			return userRepository.findAll();
		} catch (DataAccessException e) {
			logger.error("Error occurred while fetching all users", e);
			throw new UserServiceException("Unable to retrieve users. Please try again later.", e);
		}
	}

	@Override
	public Optional<User> getUserById(Long id) {
		try {
			return userRepository.findById(id);
		} catch (DataAccessException e) {
			logger.error("Error occurred while fetching user with id: " + id, e);
			throw new UserServiceException("Unable to retrieve user. Please try again later.", e);
		}
	}

	@Override
	public ResponseEntity<?> createUser(UserDTO userDTO) {
	    try {
	        // Convert UserDetailsDTO to UserDetails
	        UserDetailsDTO userDetailsDTO = userDTO.getUserDetails();
	        UserDetails userDetails = convertToUserDetails(userDetailsDTO);

	        // Convert AddressDTO list to Address entity list and set it in UserDetails
	        List<Address> addresses = convertToAddressList(userDTO.getAddresses(), userDetails);
	        userDetails.setAddresses(addresses);

	        // Convert EmergencyContactDTO list to EmergencyContact entity list and set it in UserDetails
	        List<EmergencyContact> emergencyContacts = convertToEmergencyContactList(userDTO.getEmergencyContacts(), userDetails);
	        userDetails.setEmergencyContacts(emergencyContacts);

	        // Save UserDetails first
	        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);

	        // Convert UserDTO to User and set UserDetails
	        User user = convertToUser(userDTO, savedUserDetails);

	        // Save User entity
	        User savedUser = userRepository.save(user);

	        // Convert saved user entity to UserDTO
	        UserDTO savedUserDTO = DtoConverter.convertToDto(savedUser, UserDTO.class);

	        // Generate JWT token
	        String jwtToken = jwtUtil.generateToken(savedUser.getUsername());

	        // Return UserCreationResponse with JWT token and UserDTO	        
            return ResponseEntity.ok(new AuthenticationResponse(jwtToken, savedUserDTO));


	    } catch (DataAccessException e) {
	        logger.error("Error occurred while creating user", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("404", "Unable to create user. Please try again later."));
	    }
	}
	


	private UserDetails convertToUserDetails(UserDetailsDTO userDetailsDTO) {
	    return DtoConverter.convertToEntity(userDetailsDTO, UserDetails.class);
	}


	private List<Address> convertToAddressList(List<AddressDTO> addressDTOs, UserDetails userDetails) {
	    List<Address> addresses = DtoConverter.convertToEntityList(addressDTOs, Address.class);
	    addresses.forEach(address -> address.setUserDetails(userDetails));
	    return addresses;
	}


	private List<EmergencyContact> convertToEmergencyContactList(List<EmergencyContactDTO> emergencyContactDTOs, UserDetails userDetails) {
	    return Optional.ofNullable(emergencyContactDTOs)
	            .filter(list -> !list.isEmpty())
	            .map(list -> list.stream().map(dto -> {
	                EmergencyContact contact = new EmergencyContact();
	                contact.setName(dto.getName());
	                contact.setPhoneNumber(dto.getPhoneNumber());
	                contact.setRelationship(dto.getRelationship());
	                contact.setEmail(dto.getEmail());
	                contact.setAddress(dto.getAddress());
	                contact.setPrimary(dto.isPrimary());
	                contact.setCreatedAt(new Date());
	                contact.setUserDetails(userDetails);
	                return contact;
	            }).toList())
	            .orElse(Collections.emptyList());
	}

	private User convertToUser(UserDTO userDTO, UserDetails savedUserDetails) {
	    User user = DtoConverter.convertToEntity(userDTO, User.class);
	    user.setUserDetails(savedUserDetails);
	    return user;
	}


	@Override
	public boolean deleteUser(Long id) {
		try {
			if (userRepository.existsById(id)) {
				userRepository.deleteById(id);
				return true;
			}
			return false;
		} catch (DataAccessException e) {
			logger.error("Error occurred while deleting user with id: " + id, e);
			throw new UserServiceException("Unable to delete user. Please try again later.", e);
		}
	}
}
