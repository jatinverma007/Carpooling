package com.carpooling.ums.entities;

import jakarta.persistence.*;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    @Column(name = "CreatedOn")
    private String createdAt;

    @OneToOne
    @JoinColumn(name = "user_details_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user")
    @Column(nullable = true)
    private List<UserRoleMap> userRoleMaps;
    
    @Column(name = "is_verified")
	private boolean isVerified = false;
    
    @Column(name = "verificationToken")
    private String verificationToken;


}
