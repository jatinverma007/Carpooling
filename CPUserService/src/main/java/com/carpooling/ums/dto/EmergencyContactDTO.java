package com.carpooling.ums.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactDTO {

    private Long id;
    private String name;
    private String phoneNumber;
    private String relationship;
    private String email;
    private String address;
    private boolean isPrimary;
    private Date createdAt;
}