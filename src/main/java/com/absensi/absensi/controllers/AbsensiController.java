package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.absensi.absensi.database.entities.EAbsensi;
import com.absensi.absensi.dto.AbsensiRequest;
import com.absensi.absensi.services.AbsensiService;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/absension")
public class AbsensiController {
    AbsensiService absensiService;

    public AbsensiController(AbsensiService absensiService) {
        this.absensiService = absensiService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAbsensi(@RequestBody AbsensiRequest data) {
        return absensiService.createAbsensi(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAbsensi(@PathVariable String id, @RequestBody AbsensiRequest data) {
        data.setid(UUID.fromString(id));
        return absensiService.updateAbsensi(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable("id") UUID id) {
        return absensiService.deleteAbsensi(id);
    }

    @GetMapping("/details/{id}")
    public Object getDetailsUsers(@PathVariable("id") UUID id) {
        return absensiService.getDetails(id);
    }
    
    @GetMapping("/all")
    public Object getAll() {
        return absensiService.getAll();
    }

    @GetMapping("/listting")
    public ResponseEntity<Object> getListting(
        @RequestParam(defaultValue = "0") final Integer page,
        @RequestParam(defaultValue = "10") final Integer size,
        @RequestParam(required = false) final UUID users,
        @RequestParam(required = false) final EAbsensi types
    ) {
        return absensiService.getListing(page, size, users, types);
    }
}
