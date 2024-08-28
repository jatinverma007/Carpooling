package com.carpooling.ums.entities;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String roles;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @OneToOne
    @JoinColumn(name = "user_details_id", referencedColumnName = "id")
    @JsonManagedReference
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user")
    private List<UserRoleMap> userRoleMaps;
}
