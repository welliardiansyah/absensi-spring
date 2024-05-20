package com.absensi.absensi.services;

import java.util.*;
import org.springframework.http.ResponseEntity;

import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.dto.UserPhotoRequest;

public interface UserPhotoService {
    public ResponseEntity<Object> createUserPhoto(UserPhotoRequest users);
    public ResponseEntity<Object> updateUserPhoto(UUID id, UserPhotoRequest data);
    public ResponseEntity<Object> deleteUserPhoto(UUID id);
    public Object getByUserId(UUID id);

}
