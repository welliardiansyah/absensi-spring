package com.absensi.absensi.services;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.absensi.absensi.database.entities.ERole;
import com.absensi.absensi.database.entities.RolesEntity;

public interface RoleService {
    public ResponseEntity<Object> createRole(RolesEntity rolesEntity);
    public ResponseEntity<Object> updateRole(RolesEntity rolesEntity);
    public ResponseEntity<Object> deleteRole(UUID id);
    public Object getDetails(UUID id);
    public ResponseEntity<Object> getAll();
    public ResponseEntity<Object> getListing(int page, int size, ERole search);
}
