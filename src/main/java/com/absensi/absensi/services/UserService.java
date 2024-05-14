package com.absensi.absensi.services;

import java.util.*;
import org.springframework.http.ResponseEntity;
import com.absensi.absensi.dto.UpdateUserRequest;

public interface UserService {
    public ResponseEntity<Object> updateUser(UUID id, UpdateUserRequest data);
    public ResponseEntity<Object> deleteUser(UUID id);
    public ResponseEntity<Object> getDetails(UUID id);
    public ResponseEntity<Object> getAll();
    public ResponseEntity<Object> getListing(int page, int size, String name, String nik, String role, String division);
}
