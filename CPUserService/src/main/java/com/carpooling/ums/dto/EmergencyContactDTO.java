package com.carpooling.ums.dto;

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
}
