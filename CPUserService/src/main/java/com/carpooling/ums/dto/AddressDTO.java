package com.carpooling.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

	private String id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private Long userId;
}
