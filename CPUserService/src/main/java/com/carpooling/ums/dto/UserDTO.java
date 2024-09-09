package com.carpooling.ums.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String status;
    private String roles;
    private UserDetailsDTO userDetails;
    private List<AddressDTO> addresses;
    private List<EmergencyContactDTO> emergencyContacts;
}
