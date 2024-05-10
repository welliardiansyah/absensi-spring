package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.absensi.absensi.dto.AbsensiRequest;
import com.absensi.absensi.services.AbsensiService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
}
