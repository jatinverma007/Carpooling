package com.carpooling.ums.entities;

import com.carpooling.ums.dto.UserDTO;

public class AuthenticationResponse {
    private final String jwt;
    private final UserDTO user;

    public AuthenticationResponse(String jwt, UserDTO user) {
        this.jwt = jwt;
        this.user = user;
	}

	public String getJwt() {
        return jwt;
    }
    
    public UserDTO getUserDetails() {
        return user;
    }
}
