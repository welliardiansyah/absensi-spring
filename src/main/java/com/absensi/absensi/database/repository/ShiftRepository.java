package com.absensi.absensi.database.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.absensi.absensi.database.entities.ShiftEntity;

public interface ShiftRepository extends JpaRepository<ShiftEntity, UUID>{
    Optional<ShiftEntity> findByName(String name);
    Page<ShiftEntity> findAll(Specification<ShiftEntity> specification, Pageable pageable);
    List<ShiftEntity> findByDivisions_Id(UUID divisionId);
}
