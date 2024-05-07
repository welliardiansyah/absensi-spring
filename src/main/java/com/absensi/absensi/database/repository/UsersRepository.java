package com.absensi.absensi.database.repository;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.absensi.absensi.database.entities.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}
