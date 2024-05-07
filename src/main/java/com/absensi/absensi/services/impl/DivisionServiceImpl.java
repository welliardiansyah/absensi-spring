package com.absensi.absensi.services.impl;

import java.util.*;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.repository.DivisionRepository;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.DivisionService;

@Service
public class DivisionServiceImpl implements DivisionService {
    DivisionRepository divisionRepository;

    public DivisionServiceImpl(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    @Override
    public ResponseEntity<Object> createDivision(DivisionEntity data) {
        Optional<DivisionEntity> existDivision = divisionRepository.findByName(data.getName());
        if (existDivision.isPresent()) {
            return ResponseHandler.errorResponseBuilder(
                "Nama division ini sudah digunakan!.",
                HttpStatus.NOT_FOUND
            );
        }
        try {
            DivisionEntity createData = divisionRepository.save(data);
            return ResponseHandler.successResponseBuilder(
                "Created users successfully!",
                HttpStatus.OK,
                createData
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateDivision(DivisionEntity data) {
        Optional<DivisionEntity> existDivision = divisionRepository.findById(data.getId());
        if (!existDivision.isPresent()) {
            return ResponseHandler.errorResponseBuilder(
                "Data tidak dapat ditemukan!.",
                HttpStatus.NOT_FOUND
            );
        }
        try {
            DivisionEntity updateData = divisionRepository.save(data);
            return ResponseHandler.successResponseBuilder(
                "Updated division successfully!.",
                HttpStatus.OK,
                updateData
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deleteDivision(UUID id) {
        try {
            Optional<DivisionEntity> existingRoleOptional = divisionRepository.findById(id);
            if (existingRoleOptional.isPresent()) {
                DivisionEntity existingRole = existingRoleOptional.get();
                DivisionEntity deletedRole = existingRole;
                divisionRepository.deleteById(id);

                return ResponseHandler.successResponseBuilder(
                        "Data division berhasil untuk dihapus!.",
                        HttpStatus.OK,
                        existingRole                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "Data division tidak dapat ditemukan!.",
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public Object getDetails(UUID id) {
        try {
            Optional<DivisionEntity> userOptional = divisionRepository.findById(id);
            if (userOptional.isPresent()) {
                DivisionEntity roles = userOptional.get();

                return ResponseHandler.successResponseBuilder(
                    "Get details divisions successfully!.",
                    HttpStatus.OK,
                    roles
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "Users canoot be found!.",
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<DivisionEntity> getAll = divisionRepository.findAll();
            return ResponseHandler.successResponseBuilder(
                "Get all role successfully!.",
                HttpStatus.OK,
                getAll
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getListing(int page, int size, String search) {
        try {
            if (page < 1) {
                page = 1;
            }

            int adjustedPage = page - 1;
            Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("name").ascending());

            Page<DivisionEntity> rolesPage;
            if (search != null && !search.isEmpty()) {
                rolesPage = divisionRepository.findByNameContainingIgnoreCase(search, pageable);
            } else {
                rolesPage = divisionRepository.findAll(pageable);
            }

            List<DivisionEntity> rolesList = rolesPage.getContent();

            long totalItems = rolesPage.getTotalElements();
            int totalPages = rolesPage.getTotalPages();

            Map<String, Object> response = new HashMap<>();
            response.put("data", rolesList);
            response.put("totalItems", totalItems);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);

            return ResponseEntity.ok().body(response);
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

}
