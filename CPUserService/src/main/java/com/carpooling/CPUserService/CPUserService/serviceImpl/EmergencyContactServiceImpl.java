package com.carpooling.CPUserService.CPUserService.serviceImpl;

import com.carpooling.CPUserService.CPUserService.entities.EmergencyContact;
import com.carpooling.CPUserService.CPUserService.repositories.EmergencyContactDao;
import com.carpooling.CPUserService.CPUserService.services.EmergencyContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void deleteEmergencyContact(Long id) {
        EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElse(null);
        if (emergencyContact != null) {
            emergencyContactRepository.delete(emergencyContact);
        }
    }
}

