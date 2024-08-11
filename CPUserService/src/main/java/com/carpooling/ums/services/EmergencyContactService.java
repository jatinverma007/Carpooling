package com.carpooling.ums.services;

import java.util.List;

import com.carpooling.ums.entities.EmergencyContact;

public interface EmergencyContactService {

    List<EmergencyContact> getAllEmergencyContacts();
    EmergencyContact getEmergencyContactById(Long id);
    EmergencyContact createEmergencyContact(EmergencyContact emergencyContact);
    EmergencyContact updateEmergencyContact(Long id, EmergencyContact emergencyContactDetails);
    boolean deleteEmergencyContact(Long id);

}
