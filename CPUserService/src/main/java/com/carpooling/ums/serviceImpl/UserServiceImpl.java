package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;

import com.carpooling.ums.dto.UserDTO;
import com.carpooling.ums.dto.UserDetailsDTO;
import com.carpooling.ums.entities.Address;
import com.carpooling.ums.entities.User;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.exceptions.UserServiceException;
import com.carpooling.ums.repositories.UserDao;
import com.carpooling.ums.repositories.UserDetailsDao;
import com.carpooling.ums.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDao userRepository;

	@Autowired
	private UserDetailsDao userDetailsRepository;

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
	public User createUser(UserDTO userDTO) {
		try {
			// Convert UserDetailsDTO to UserDetails
			UserDetailsDTO userDetailsDTO = userDTO.getUserDetails();
			UserDetails userDetails = new UserDetails();
			userDetails.setFirstname(userDetailsDTO.getFirstname());
			userDetails.setLastname(userDetailsDTO.getLastname());
			userDetails.setDob(userDetailsDTO.getDob());
			userDetails.setGender(userDetailsDTO.getGender());
			userDetails.setProfilePicture(userDetailsDTO.getProfilePicture());
			userDetails.setBio(userDetailsDTO.getBio());
			userDetails.setRegistrationDate(userDetailsDTO.getRegistrationDate());
			userDetails.setLastLoginDate(userDetailsDTO.getLastLoginDate());

			// Convert AddressDTO list to Address entity list and set it in UserDetails
			List<Address> addresses = Optional.ofNullable(userDTO.getAddresses()).filter(list -> !list.isEmpty())
					.map(list -> list.stream().map(addressDTO -> {
						Address address = new Address();
						address.setStreet(addressDTO.getStreet());
						address.setCity(addressDTO.getCity());
						address.setState(addressDTO.getState());
						address.setZipCode(addressDTO.getZipCode());
						address.setUserDetails(userDetails);
						return address;
					}).toList()).orElse(Collections.emptyList()); // No default address if user provides an address list

			userDetails.setAddresses(addresses);

			// Save UserDetails first
			UserDetails savedUserDetails = userDetailsRepository.save(userDetails);

			// Convert UserDTO to User
			User user = new User();
			user.setUsername(userDTO.getUsername());
			user.setPassword(userDTO.getPassword());
			user.setRoles(userDTO.getRoles());
			user.setStatus(userDTO.getStatus());

			// Set UserDetails to User
			user.setUserDetails(savedUserDetails);

			// Save User entity
			return userRepository.save(user);
		} catch (DataAccessException e) {
			logger.error("Error occurred while creating user", e);
			throw new UserServiceException("Unable to create user. Please try again later.", e);
		}
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
