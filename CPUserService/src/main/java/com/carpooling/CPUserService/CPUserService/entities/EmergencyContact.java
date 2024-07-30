package com.carpooling.CPUserService.CPUserService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emergency_contact")
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String relationship;
    private String phoneNumber;
    private String email;
    private String address;
    private boolean isPrimary;
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private UserDetails userDetails;
}
