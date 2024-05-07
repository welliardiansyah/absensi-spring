package com.absensi.absensi.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.absensi.absensi.database.entities.RolesEntity;
import com.absensi.absensi.services.RoleService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> createUsers(@RequestBody RolesEntity data) {
        return roleService.createRole(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable String id, @RequestBody RolesEntity data) {
        data.setId(UUID.fromString(id));
        return roleService.updateRole(data);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable("id") UUID id) {
        return roleService.deleteRole(id);
    }

    @GetMapping("{id}")
    public Object getDetailsUsers(@PathVariable("id") UUID id) {
        return roleService.getDetails(id);
    }

    @GetMapping("/all")
    public Object getAll() {
        return roleService.getAll();
    }

    @GetMapping("/listting")
    public ResponseEntity<Object> getListting(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        return roleService.getListing(page, size, search);
    }

}
