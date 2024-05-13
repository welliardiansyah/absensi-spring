package com.absensi.absensi.services;

import java.util.UUID;
import org.springframework.http.ResponseEntity;

import com.absensi.absensi.database.entities.EAbsensi;
import com.absensi.absensi.dto.AbsensiRequest;

public interface AbsensiService {
    public ResponseEntity<Object> createAbsensi(AbsensiRequest data);
    public ResponseEntity<Object> updateAbsensi(AbsensiRequest data);
    public ResponseEntity<Object> deleteAbsensi(UUID id);
    Object getDetails(UUID id);
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getListing(int page, int size, UUID users, EAbsensi types);
}
