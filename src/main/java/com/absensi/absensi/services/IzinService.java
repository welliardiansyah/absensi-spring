package com.absensi.absensi.services;

import java.util.*;
import org.springframework.http.ResponseEntity;

import com.absensi.absensi.database.entities.EIzin;
import com.absensi.absensi.dto.IzinRequest;

public interface IzinService {
    public ResponseEntity<Object> createIzin(IzinRequest data);
    public ResponseEntity<Object> updateIzin(UUID id, IzinRequest data);
    public ResponseEntity<Object> deleteIzin(UUID id);
    public Object getDetails(UUID id);
    public ResponseEntity<Object> getAll();
    public ResponseEntity<Object> getListing(int page, int size, EIzin name, UUID users);
}
