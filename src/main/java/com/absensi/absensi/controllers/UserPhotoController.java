package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.absensi.absensi.dto.UserPhotoRequest;
import com.absensi.absensi.services.UserPhotoService;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/user/photo")
public class UserPhotoController {
    @Autowired
    UserPhotoService userPhotoService;

    public UserPhotoController(UserPhotoService userPhotoService) {
        this.userPhotoService = userPhotoService;
    }

    @PostMapping("")
    public ResponseEntity<Object> create(
        @RequestParam("photo") MultipartFile photo,
        @RequestParam("users") UUID users
        ) {
        UserPhotoRequest convert = new UserPhotoRequest();
        convert.setPhoto(photo);
        convert.setUsers(users);
        return userPhotoService.createUserPhoto(convert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
        @PathVariable("id") UUID id, 
        @RequestParam("photo") MultipartFile photo,
        @RequestParam("users") UUID users
        ) {
            UserPhotoRequest convert = new UserPhotoRequest();
            convert.setPhoto(photo);
            convert.setUsers(users);
        return userPhotoService.updateUserPhoto(id, convert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleted(@PathVariable("id") UUID id) {
        return userPhotoService.deleteUserPhoto(id);   
    }

    @GetMapping("/users/{id}")
    public Object gettingByUsers(@PathVariable("id") UUID id) {
        return userPhotoService.getByUserId(id);   
    }
}
