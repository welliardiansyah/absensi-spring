package com.absensi.absensi.services;

import java.util.*;
import org.springframework.http.ResponseEntity;

import com.absensi.absensi.database.entities.EDivision;
import com.absensi.absensi.dto.DivisionRequest;

public interface DivisionService {
    public ResponseEntity<Object> createDivision(DivisionRequest data);
    public ResponseEntity<Object> updateDivision(DivisionRequest data);
    public ResponseEntity<Object> deleteDivision(UUID id);
    ResponseEntity<Object> getDetails(UUID id);
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getListing(int page, int size, EDivision search, UUID superior, UUID manager, UUID leader);
}
