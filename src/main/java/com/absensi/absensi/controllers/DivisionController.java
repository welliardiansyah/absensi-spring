package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.absensi.absensi.database.entities.EDivision;
import com.absensi.absensi.dto.DivisionRequest;
import com.absensi.absensi.services.DivisionService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1/divisions")
public class DivisionController {
    DivisionService divisionService;

    public DivisionController(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createDivisions(@RequestBody DivisionRequest data) {
        return divisionService.createDivision(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDivisions(@PathVariable("id") UUID id, @RequestBody DivisionRequest data) {
        data.setId(id);
        return divisionService.updateDivision(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDivisions(@PathVariable("id") UUID id) {
        return divisionService.deleteDivision(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetail(@PathVariable("id") UUID id) {
        return divisionService.getDetails(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return divisionService.getAll();
    }

    @GetMapping("/listting")
    public ResponseEntity<Object> getListting(
        @RequestParam(defaultValue = "0") final Integer page,
        @RequestParam(defaultValue = "10") final Integer size,
        @RequestParam(required = false) final EDivision search,
        @RequestParam(required = false) final UUID superior,
        @RequestParam(required = false) final UUID manager,
        @RequestParam(required = false) final UUID leader
    ) {
        return divisionService.getListing(page, size, search, superior, manager, leader);
    }
}
