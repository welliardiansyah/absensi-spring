package com.absensi.absensi.database.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.absensi.absensi.database.entities.AbsensiEntity;

public interface AbsensiRepository extends JpaRepository<AbsensiEntity, UUID> {
}
