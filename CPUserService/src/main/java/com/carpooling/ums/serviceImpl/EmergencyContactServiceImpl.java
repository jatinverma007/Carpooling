package com.carpooling.ums.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.repositories.EmergencyContactDao;
import com.carpooling.ums.services.EmergencyContactService;

import java.util.List;
import java.util.Optional;

@Service
public class EmergencyContactServiceImpl implements EmergencyContactService {

    @Autowired
    private EmergencyContactDao emergencyContactRepository;

    @Override
    public List<EmergencyContact> getAllEmergencyContacts() {
        return emergencyContactRepository.findAll();
    }

    @Override
    public EmergencyContact getEmergencyContactById(Long id) {
        return emergencyContactRepository.findById(id).orElse(null);
    }

    @Override
    public EmergencyContact createEmergencyContact(EmergencyContact emergencyContact) {
        return emergencyContactRepository.save(emergencyContact);
    }

    @Override
    public EmergencyContact updateEmergencyContact(Long id, EmergencyContact emergencyContactDetails) {
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
    }

    @Override
    public boolean deleteEmergencyContact(Long id) {
        Optional<EmergencyContact> emergencyContactOptional = emergencyContactRepository.findById(id);
        if (emergencyContactOptional.isPresent()) {
            emergencyContactRepository.delete(emergencyContactOptional.get());
            return true;
        } else {
            return false;
        }
    }

}

