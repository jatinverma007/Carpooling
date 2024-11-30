package com.carpooling.ums.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refreshable_properties")
public class RefreshableProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use backticks for the `key` field to avoid conflicts with reserved keywords
    @Column(name = "`key`", nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    private String value;

    private String description;

    @Column(nullable = false)
    private Boolean active;

    // You can also add custom methods if necessary
}
