package com.carpooling.ums.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street", length = 255, nullable = false)
    private String street;

    @Column(name = "city", length = 255, nullable = false)
    private String city;

    @Column(name = "state", length = 255, nullable = false)
    private String state;

    @Column(name = "zip_code", length = 20, nullable = false)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserDetails userDetails;
}
