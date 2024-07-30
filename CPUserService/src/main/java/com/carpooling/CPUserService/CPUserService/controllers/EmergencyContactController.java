package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.EmergencyContact;
import com.carpooling.CPUserService.CPUserService.repositories.EmergencyContactRepository;

import java.util.List;

@RestController
@RequestMapping("/api/emergencycontacts")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @GetMapping
    public List<EmergencyContact> getAllEmergencyContacts() {
        return emergencyContactRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getEmergencyContactById(@PathVariable Long id) {
        EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElse(null);
        return emergencyContact != null ? ResponseEntity.ok(emergencyContact) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public EmergencyContact createEmergencyContact(@RequestBody EmergencyContact emergencyContact) {
        return emergencyContactRepository.save(emergencyContact);
    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<EmergencyContact> updateEmergencyContact(@PathVariable Long id, @RequestBody EmergencyContact emergencyContactDetails) {
//        EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElse(null);
//        if (emergencyContact != null) {
//            emergencyContact.setName(emergencyContactDetails.getName());
//            emergencyContact.setRelationship(emergencyContactDetails.getRelationship());
//            emergencyContact.setPhoneNumber(emergencyContactDetails.getPhoneNumber());
//            emergencyContact.setEmail(emergencyContactDetails.getEmail());
//            emergencyContact.setAddress(emergencyContactDetails.getAddress());
//            emergencyContact.setPrimary(emergencyContactDetails.isPrimary());
//            EmergencyContact updatedEmergencyContact = emergencyContactRepository.save(emergencyContact);
//            return ResponseEntity.ok(updatedEmergencyContact);
//        }
//        return ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Long id) {
        EmergencyContact emergencyContact = emergencyContactRepository.findById(id).orElse(null);
        if (emergencyContact != null) {
            emergencyContactRepository.delete(emergencyContact);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

