package com.absensi.absensi.services.impl;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.absensi.absensi.database.entities.ERole;
import com.absensi.absensi.database.entities.RolesEntity;
import com.absensi.absensi.database.repository.RolesRepository;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.RoleService;

import jakarta.persistence.criteria.Predicate;

@Service
public class RoleServiceImpl implements RoleService {
    RolesRepository rolesRepository;

    public RoleServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public ResponseEntity<Object> createRole(RolesEntity data) {
        Optional<RolesEntity> existingRole = rolesRepository.findByName(data.getName());
        if (existingRole.isPresent()) {
            return ResponseHandler.errorResponseBuilder(
                "Nama role ini sudah digunakan!.",
                HttpStatus.NOT_FOUND
            );
        }
        try {
            RolesEntity createData = rolesRepository.save(data);
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
    public ResponseEntity<Object> updateRole(RolesEntity data) {
        if (!rolesRepository.existsById(data.getId())) {
            return ResponseHandler.errorResponseBuilder(
                    "Data roles tidak dapat ditemukan.",
                    HttpStatus.NOT_FOUND
            );
        }
        try {
            RolesEntity updateData = rolesRepository.save(data);
            return ResponseHandler.successResponseBuilder(
                "Updated roles successfully!.",
                HttpStatus.OK,
                updateData
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deleteRole(UUID id) {
        try {
            Optional<RolesEntity> existingRoleOptional = rolesRepository.findById(id);
            if (existingRoleOptional.isPresent()) {
                RolesEntity existingRole = existingRoleOptional.get();
                RolesEntity deletedRole = existingRole;
                rolesRepository.deleteById(id);

                return ResponseHandler.successResponseBuilder(
                        "Data role berhasil untuk dihapus!.",
                        HttpStatus.OK,
                        existingRole                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "Data role tidak dapat ditemukan!.",
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Failed to delete role!. " + e.getMessage());
        }
    }

    @Override
    public Object getDetails(UUID id) {
        try {
            Optional<RolesEntity> userOptional = rolesRepository.findById(id);
            if (userOptional.isPresent()) {
                RolesEntity roles = userOptional.get();

                return ResponseHandler.successResponseBuilder(
                    "Get details role successfully!.",
                    HttpStatus.OK,
                    roles
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "Role canoot be found!.",
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
            List<RolesEntity> getAll = rolesRepository.findAll();
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
    public ResponseEntity<Object> getListing(int page, int size, ERole search) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            Specification<RolesEntity> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (search != null) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), search.toString()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<RolesEntity> divisionsPage = rolesRepository.findAll(specification, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("data", divisionsPage.getContent());
            response.put("totalItems", divisionsPage.getTotalElements());
            response.put("totalPages", divisionsPage.getTotalPages());
            response.put("currentPage", divisionsPage.getNumber() + 1);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new NotFoundException("Data not found!." + e.getMessage());
        }
    }

}
