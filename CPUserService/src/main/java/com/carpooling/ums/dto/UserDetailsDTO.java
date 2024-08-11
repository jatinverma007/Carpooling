package com.carpooling.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private Date dob;
    private String gender;
    private String profilePicture;
    private String bio;
    private Date registrationDate;
    private Date lastLoginDate;
    
    private List<EmergencyContactDTO> emergencyContacts;
    private List<AddressDTO> addresses;
    private UsersTncDTO usersTnc;

}
