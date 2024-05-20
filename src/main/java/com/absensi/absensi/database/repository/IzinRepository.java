package com.absensi.absensi.database.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.absensi.absensi.database.entities.EIzin;
import com.absensi.absensi.database.entities.IzinEntity;
import com.absensi.absensi.database.entities.UsersEntity;

public interface IzinRepository extends JpaRepository<IzinEntity, UUID> {
    Page<IzinEntity> findAll(Specification<IzinEntity> specification, Pageable pageable);
}
