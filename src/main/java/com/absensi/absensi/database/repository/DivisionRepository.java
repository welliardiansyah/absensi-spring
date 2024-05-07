package com.absensi.absensi.database.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.EDivision;


public interface DivisionRepository extends JpaRepository<DivisionEntity, UUID> {
    Optional<DivisionEntity> findByName(EDivision name);
    Page<DivisionEntity> findByNameContainingIgnoreCase(String search, Pageable pageable);
}

