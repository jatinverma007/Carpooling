package com.carpooling.CPUserService.CPUserService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carpooling.CPUserService.CPUserService.entities.EmergencyContact;
import com.carpooling.CPUserService.CPUserService.services.EmergencyContactService;

import java.util.List;

@RestController
@RequestMapping("/emergencycontacts")
public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    @GetMapping
    public List<EmergencyContact> getAllEmergencyContacts() {
        return emergencyContactService.getAllEmergencyContacts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContact> getEmergencyContactById(@PathVariable Long id) {
        EmergencyContact emergencyContact = emergencyContactService.getEmergencyContactById(id);
        return emergencyContact != null ? ResponseEntity.ok(emergencyContact) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public EmergencyContact createEmergencyContact(@RequestBody EmergencyContact emergencyContact) {
        return emergencyContactService.createEmergencyContact(emergencyContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContact> updateEmergencyContact(@PathVariable Long id, @RequestBody EmergencyContact emergencyContactDetails) {
        EmergencyContact updatedEmergencyContact = emergencyContactService.updateEmergencyContact(id, emergencyContactDetails);
        return updatedEmergencyContact != null ? ResponseEntity.ok(updatedEmergencyContact) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencyContact(@PathVariable Long id) {
        emergencyContactService.deleteEmergencyContact(id);
        return ResponseEntity.noContent().build();
    }
}

