package com.carpooling.ums.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonIgnore
    private String password;
    private String status;
    private String roles;
    private boolean isVerified;
    private transient String verificationToken;
   
    @JsonProperty("verificationToken")
    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    // Custom method to set verificationToken based on context
    public void setConditionalVerificationToken(boolean include, String token) {
        if (include) {
            this.verificationToken = token;
        } else {
            this.verificationToken = null;
        }
    }
}
