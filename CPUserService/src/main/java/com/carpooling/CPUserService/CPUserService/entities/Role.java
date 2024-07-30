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
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roleName;

    private Date createdAt;
    
    private boolean isActive;

    @OneToMany(mappedBy = "role")
    private List<UserRoleMap> userRoleMaps;

    @OneToMany(mappedBy = "role")
    private List<RolesPermissionAction> rolesPermissionActions;

}
