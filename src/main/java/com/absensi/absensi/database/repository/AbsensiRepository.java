package com.absensi.absensi.database.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.absensi.absensi.database.entities.AbsensiEntity;
import com.absensi.absensi.database.entities.EAbsensi;
import com.absensi.absensi.database.entities.UsersEntity;

public interface AbsensiRepository extends JpaRepository<AbsensiEntity, UUID> {
    Page<AbsensiEntity> findByUsers(UsersEntity users, Pageable pageable);
    Page<AbsensiEntity> findByTypes(EAbsensi types, Pageable pageable);
    Page<AbsensiEntity> findByUsersAndTypes(UsersEntity users, EAbsensi types, Pageable paging);
}
