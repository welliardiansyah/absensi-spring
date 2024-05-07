package com.absensi.absensi.services;

import java.util.*;
import org.springframework.http.ResponseEntity;
import com.absensi.absensi.database.entities.DivisionEntity;

public interface DivisionService {
    public ResponseEntity<Object> createDivision(DivisionEntity rolesEntity);
    public ResponseEntity<Object> updateDivision(DivisionEntity rolesEntity);
    public ResponseEntity<Object> deleteDivision(UUID id);
    Object getDetails(UUID id);
    ResponseEntity<Object> getAll();
    ResponseEntity<Object> getListing(int page, int size, String search);
}
