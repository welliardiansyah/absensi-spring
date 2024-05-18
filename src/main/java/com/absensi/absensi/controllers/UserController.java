package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.absensi.absensi.dto.UpdateUserRequest;
import com.absensi.absensi.services.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/updated/{id}")
    public ResponseEntity<Object> updateUsers(@PathVariable UUID id, @RequestBody UpdateUserRequest data) {
        return userService.updateUser(id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetail(@PathVariable UUID id) {
        return userService.getDetails(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return userService.getAll();
    }

    @GetMapping("/listting")
    public ResponseEntity<Object> getListting(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String nik,
        @RequestParam(required = false) String roles,
        @RequestParam(required = false) String division
    ) {
        return userService.getListing(page, size, name, nik, roles, division);
    }
}
