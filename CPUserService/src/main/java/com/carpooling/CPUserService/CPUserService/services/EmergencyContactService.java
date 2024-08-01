package com.carpooling.CPUserService.CPUserService.services;

import java.util.List;

import com.carpooling.CPUserService.CPUserService.entities.EmergencyContact;

public interface EmergencyContactService {

    List<EmergencyContact> getAllEmergencyContacts();
    EmergencyContact getEmergencyContactById(Long id);
    EmergencyContact createEmergencyContact(EmergencyContact emergencyContact);
    EmergencyContact updateEmergencyContact(Long id, EmergencyContact emergencyContactDetails);
    void deleteEmergencyContact(Long id);

}
