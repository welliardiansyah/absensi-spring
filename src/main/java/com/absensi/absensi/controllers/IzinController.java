package com.absensi.absensi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        @RequestParam(value = "photo", required = false) MultipartFile photo,
        @RequestParam("name") EIzin name,
        @RequestParam("dateStart") String dateStart,
        @RequestParam("dateEnd") String dateEnd,
        @RequestParam("description") String description,
        @RequestParam("users") UUID users,
        @RequestParam("is_actived") Boolean is_actived) throws IllegalStateException, IOException {
        System.out.println(users);
            IzinRequest data = new IzinRequest();
            data.setName(name);
            data.setDateStart(dateStart);
            data.setDateEnd(dateEnd);
            data.setDescription(description);
            data.setPhoto(photo);
            data.setUsers(users);
            data.setIs_actived(is_actived);
            return izinService.createIzin(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateIzin(
        @PathVariable("id") UUID id,
        @RequestParam(value = "photo", required = false) MultipartFile photo,
        @RequestParam(value = "name") EIzin name,
        @RequestParam(value = "dateStart") String dateStart,
        @RequestParam(value = "dateEnd") String dateEnd,
        @RequestParam(value = "description") String description,
        @RequestParam(value = "users") UUID users,
        @RequestParam(value = "is_actived") Boolean is_actived) throws IllegalStateException, IOException {
            IzinRequest data = new IzinRequest();
            data.setName(name);
            data.setDateStart(dateStart);
            data.setDateEnd(dateEnd);
            data.setDescription(description);
            data.setPhoto(photo);
            data.setUsers(users);
            data.setIs_actived(is_actived);
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
