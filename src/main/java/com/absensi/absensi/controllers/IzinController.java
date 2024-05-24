package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.absensi.absensi.database.entities.EIzin;
import com.absensi.absensi.dto.IzinRequest;
import com.absensi.absensi.services.IzinService;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1/izin")
public class IzinController {
    @Autowired
    IzinService izinService;

    public IzinController(IzinService izinService) {
        this.izinService = izinService;
    }

    @PostMapping("")
    public ResponseEntity<Object> createIzin(
        @ModelAttribute IzinRequest data
        ) throws IllegalStateException, IOException {
            return izinService.createIzin(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateIzin(
        @PathVariable("id") UUID id,
        @ModelAttribute IzinRequest data) throws IllegalStateException, IOException {
            return izinService.updateIzin(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteIzin(@PathVariable("id") UUID id) {
        return izinService.deleteIzin(id);
    }

    @GetMapping("/{id}")
    public Object getDetail(@PathVariable("id") UUID id) {
        return izinService.getDetails(id);
    }
    
    @GetMapping("/all")
    public Object getAll() {
        return izinService.getAll();
    }

    @GetMapping("/listting")
    public ResponseEntity<Object> getListting(
        @RequestParam(defaultValue = "0") final Integer page,
        @RequestParam(defaultValue = "10") final Integer size,
        @RequestParam(required = false) final UUID users,
        @RequestParam(required = false) final EIzin name) {
        return izinService.getListing(page, size, name, users);
    }
    
}
