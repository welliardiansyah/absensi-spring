package com.absensi.absensi.database.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.absensi.absensi.database.entities.UserPhotoEntity;
import com.absensi.absensi.database.entities.UsersEntity;

public interface UserPhotoRepository extends JpaRepository<UserPhotoEntity, UUID>{
    List<UserPhotoEntity> findByUsers(UsersEntity users);
}
