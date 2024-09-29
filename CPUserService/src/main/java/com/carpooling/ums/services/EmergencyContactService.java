package com.carpooling.ums.services;

import java.util.List;
import java.util.Optional;

import com.carpooling.ums.dto.EmergencyContactDTO;
import com.carpooling.ums.entities.EmergencyContact;
import com.carpooling.ums.entities.UserDetails;

public interface EmergencyContactService {

    List<EmergencyContact> getAllEmergencyContacts();
    EmergencyContact getEmergencyContactById(Long id);
    EmergencyContact createEmergencyContact(EmergencyContact emergencyContact);
    EmergencyContact updateEmergencyContact(Long id, EmergencyContact emergencyContactDetails);
    boolean deleteEmergencyContact(Long id);
   
    //New
    List<EmergencyContact> getEmergencyContactsByUserDetails(UserDetails userDetails);
    EmergencyContact saveEmergencyContact(EmergencyContactDTO emergencyContactDTO, UserDetails userDetails);
	Optional<EmergencyContact> findById(Long id);

}
