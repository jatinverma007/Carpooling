package com.carpooling.ums.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carpooling.ums.entities.RefreshableProperty;

public interface RefreshablePropertyDao extends JpaRepository<RefreshableProperty, Long> {
    List<RefreshableProperty> findAllByActiveTrue();
    Optional<RefreshableProperty> findByKey(String key);
}