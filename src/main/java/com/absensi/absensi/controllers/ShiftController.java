package com.absensi.absensi.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.absensi.absensi.dto.ShiftRequest;
import com.absensi.absensi.services.ShiftService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/shift")
public class ShiftController {
    @Autowired
    ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @PostMapping("")
    public ResponseEntity<Object> createShift(@RequestBody ShiftRequest data) {
        return shiftService.createShift(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateShift(@PathVariable UUID id, @RequestBody ShiftRequest data) {
        return shiftService.updateShift(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteShift(@PathVariable UUID id) {
        return shiftService.deleteShift(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetail(@PathVariable("id") UUID id) {
        return shiftService.getDetails(id);
    }
    
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return shiftService.getAll();
    }

    @GetMapping("/listting")
    public ResponseEntity<Object> getListting(
        @RequestParam(defaultValue = "0") final Integer page,
        @RequestParam(defaultValue = "10") final Integer size,
        @RequestParam(required = false) final String search,
        @RequestParam(required = false) final UUID division
    ) {
        return shiftService.getListing(page, size, search, division);
    }

    @GetMapping("/division/{id}")
    public ResponseEntity<Object> getShiftsByDivisionId(@PathVariable("id") UUID id) {
        return shiftService.getShiftsByDivisionId(id);
    }
    
}
