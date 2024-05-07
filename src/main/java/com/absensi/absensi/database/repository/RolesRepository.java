package com.absensi.absensi.database.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.absensi.absensi.database.entities.ERole;
import com.absensi.absensi.database.entities.RolesEntity;

public interface RolesRepository extends JpaRepository<RolesEntity, UUID> {
    Optional<RolesEntity> findByName(ERole name);
    Page<RolesEntity> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
