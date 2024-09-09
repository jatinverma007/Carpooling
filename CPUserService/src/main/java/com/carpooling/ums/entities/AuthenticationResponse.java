package com.carpooling.ums.entities;

import com.carpooling.ums.dto.UserDetailsDTO;

public class AuthenticationResponse {
    private final String jwt;
    private final UserDetailsDTO userDetails;

    public AuthenticationResponse(String jwt, UserDetailsDTO userDetails) {
        this.jwt = jwt;
        this.userDetails = userDetails;
	}

	public String getJwt() {
        return jwt;
    }
    
    public UserDetailsDTO getUserDetails() {
        return userDetails;
    }
}
