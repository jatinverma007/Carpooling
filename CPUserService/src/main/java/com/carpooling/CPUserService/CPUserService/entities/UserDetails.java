package com.carpooling.CPUserService.CPUserService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userdetails")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String address;
    private Date dob;
    private String gender;
    private String profilePicture;
    private String bio;
    private Date registrationDate;
    private Date lastLoginDate;
    private Date createdAt;

    @OneToOne(mappedBy = "userDetails")
    private User user;

    @OneToMany(mappedBy = "userDetails")
    private List<EmergencyContact> emergencyContacts;

}

