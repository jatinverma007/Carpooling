package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.dao.DataAccessException;

import com.carpooling.ums.dto.EmergencyContactDTO;
import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.entities.UserDetails;
import com.carpooling.ums.exceptions.EmergencyContactServiceException;
import com.carpooling.ums.repositories.EmergencyContactDao;
import com.carpooling.ums.services.EmergencyContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private static final Logger logger = LoggerFactory.getLogger(EmergencyContactServiceImpl.class);

    @Autowired
    private EmergencyContactDao emergencyContactRepository;

    @Override
    public List<EmergencyContact> getAllEmergencyContacts() {
        try {
            return emergencyContactRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching all emergency contacts", e);
            throw new EmergencyContactServiceException("Unable to retrieve emergency contacts. Please try again later.", e);
        }
    }

    @Override
    public EmergencyContact getEmergencyContactById(Long id) {
        try {
            return emergencyContactRepository.findById(id).orElse(null);
        } catch (DataAccessException e) {
            logger.error("Error occurred while fetching emergency contact with id: " + id, e);
            throw new EmergencyContactServiceException("Unable to retrieve emergency contact. Please try again later.", e);
        }
    }

    @Override
    public EmergencyContact createEmergencyContact(EmergencyContact emergencyContact) {
        try {
            return emergencyContactRepository.save(emergencyContact);
        } catch (DataAccessException e) {
            logger.error("Error occurred while creating emergency contact", e);
            throw new EmergencyContactServiceException("Unable to create emergency contact. Please try again later.", e);
        }
    }

    @Override
    public EmergencyContact updateEmergencyContact(Long id, EmergencyContact emergencyContactDetails) {
        try {
            EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElse(null);
            if (emergencyContact != null) {
                emergencyContact.setName(emergencyContactDetails.getName());
                emergencyContact.setRelationship(emergencyContactDetails.getRelationship());
                emergencyContact.setPhoneNumber(emergencyContactDetails.getPhoneNumber());
                emergencyContact.setEmail(emergencyContactDetails.getEmail());
                emergencyContact.setAddress(emergencyContactDetails.getAddress());
                emergencyContact.setPrimary(emergencyContactDetails.isPrimary());
                return emergencyContactRepository.save(emergencyContact);
            }
            return null;
        } catch (DataAccessException e) {
            logger.error("Error occurred while updating emergency contact with id: " + id, e);
            throw new EmergencyContactServiceException("Unable to update emergency contact. Please try again later.", e);
        }
    }

    @Override
    public boolean deleteEmergencyContact(Long id) {
        try {
            Optional<EmergencyContact> emergencyContactOptional = emergencyContactRepository.findById(id);
            if (emergencyContactOptional.isPresent()) {
                emergencyContactRepository.delete(emergencyContactOptional.get());
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            logger.error("Error occurred while deleting emergency contact with id: " + id, e);
            throw new EmergencyContactServiceException("Unable to delete emergency contact. Please try again later.", e);
        }
    }

	@Override
	public List<EmergencyContact> getEmergencyContactsByUserDetails(UserDetails userDetails) {
        return emergencyContactRepository.findByUserDetails(userDetails);
	}

    @Override
    public EmergencyContact saveEmergencyContact(EmergencyContactDTO emergencyContactDTO, UserDetails userDetails) {
        EmergencyContact emergencyContact = new EmergencyContact();
        emergencyContact.setName(emergencyContactDTO.getName());
        emergencyContact.setRelationship(emergencyContactDTO.getRelationship());
        emergencyContact.setPhoneNumber(emergencyContactDTO.getPhoneNumber());
        emergencyContact.setEmail(emergencyContactDTO.getEmail());
        emergencyContact.setAddress(emergencyContactDTO.getAddress());
        emergencyContact.setPrimary(emergencyContactDTO.isPrimary());
        emergencyContact.setUserDetails(userDetails);
        emergencyContact.setCreatedAt(new Date());

        return emergencyContactRepository.save(emergencyContact);
    }

	@Override
	public Optional<EmergencyContact> findById(Long id) {
	    return emergencyContactRepository.findById(id);
	}
}
