package com.absensi.absensi.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.absensi.absensi.database.entities.AbsensiEntity;
import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.AbsensiRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.AbsensiRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.AbsensiService;

@Service
public class AbsensiServiceImpl implements AbsensiService{
    AbsensiRepository absensiRepository;

    @Autowired
    private UsersRepository usersRepository;

    public AbsensiServiceImpl(AbsensiRepository absensiRepository) {
        this.absensiRepository = absensiRepository;
    }

    @Override
    public ResponseEntity<Object> createAbsensi(AbsensiRequest data) {
        Optional<UsersEntity> optionalUser = usersRepository.findById(data.getUsers());
        if (optionalUser.isEmpty()) {
            return ResponseHandler.errorResponseBuilder(
                    "User tidak ditemukan!.",
                    HttpStatus.NOT_FOUND
            );
        }
        UsersEntity user = optionalUser.get();
        
        try {
            AbsensiEntity absension = new AbsensiEntity(
                null, 
                data.getAbsensiType(),
                data.getTime(),
                data.getTime_start(),
                data.getTime_end(),
                data.getLatitude(),
                data.getLongitude(),
                data.getDescription(),
                user,
                null, 
                null
            );
            AbsensiEntity saveData = absensiRepository.save(absension);
            saveData.getUsers().setPassword(null);

            return ResponseHandler.successResponseBuilder(
                "Absensi berhasil dibuat!.",
                HttpStatus.OK,
                saveData
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateAbsensi(AbsensiEntity rolesEntity) {
        try {
            return null;
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deleteAbsensi(UUID id) {
        try {
            return null;
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public Object getDetails(UUID id) {
        try {
            return null;
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            return null;
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getListing(int page, int size, String search) {
        try {
            return null;
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }
}
