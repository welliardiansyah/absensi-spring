package com.absensi.absensi.services;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.absensi.absensi.database.entities.RolesEntity;

public interface RoleService {
    public ResponseEntity<Object> createRole(RolesEntity rolesEntity);
    public ResponseEntity<Object> updateRole(RolesEntity rolesEntity);
    public ResponseEntity<Object> deleteRole(UUID id);
    Object getDetails(UUID id);
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getListing(int page, int size, String search);
}
