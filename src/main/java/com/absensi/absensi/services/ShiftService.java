package com.absensi.absensi.services;

import java.util.*;
import org.springframework.http.ResponseEntity;
import com.absensi.absensi.dto.ShiftRequest;

public interface ShiftService {
    public ResponseEntity<Object> createShift(ShiftRequest shiftEntity);
    public ResponseEntity<Object> updateShift(UUID id, ShiftRequest shiftEntity);
    public ResponseEntity<Object> deleteShift(UUID id);
    public ResponseEntity<Object> getDetails(UUID id);
    public ResponseEntity<Object> getAll();
    public ResponseEntity<Object> getListing(int page, int size, String search, UUID division);
    public ResponseEntity<Object> getShiftsByDivisionId(UUID id);
}
