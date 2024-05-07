package com.absensi.absensi.services;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.absensi.absensi.database.entities.AbsensiEntity;
import com.absensi.absensi.dto.AbsensiRequest;

public interface AbsensiService {
    public ResponseEntity<Object> createAbsensi(AbsensiRequest data);
    public ResponseEntity<Object> updateAbsensi(AbsensiEntity rolesEntity);
    public ResponseEntity<Object> deleteAbsensi(UUID id);
    Object getDetails(UUID id);
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getListing(int page, int size, String search);
}
