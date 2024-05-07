package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.absensi.absensi.database.entities.DivisionEntity;
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

    @PostMapping("/")
    public ResponseEntity<Object> createDivisions(@RequestBody DivisionEntity data) {
        return divisionService.createDivision(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDivisions(@PathVariable String id, @RequestBody DivisionEntity data) {
        data.setId(UUID.fromString(id));
        return divisionService.updateDivision(data);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable("id") UUID id) {
        return divisionService.deleteDivision(id);
    }

    @GetMapping("{id}")
    public Object getDetailsUsers(@PathVariable("id") UUID id) {
        return divisionService.getDetails(id);
    }

    @GetMapping("/all")
    public Object getAll() {
        return divisionService.getAll();
    }

    @GetMapping("/listting")
    public ResponseEntity<Object> getListting(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        return divisionService.getListing(page, size, search);
    }

}
