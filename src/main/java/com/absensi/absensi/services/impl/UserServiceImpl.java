package com.absensi.absensi.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.EDivision;
import com.absensi.absensi.database.entities.ERole;
import com.absensi.absensi.database.entities.RolesEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.DivisionRepository;
import com.absensi.absensi.database.repository.RolesRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.UpdateUserRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.UserService;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    DivisionRepository divisionRepository;

    public UserServiceImpl(UsersRepository usersRepository, RolesRepository rolesRepository, DivisionRepository divisionRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public ResponseEntity<Object> updateUser(UUID id, UpdateUserRequest data) {
        Optional<UsersEntity> getUsers = usersRepository.findById(id);
        if (!getUsers.isPresent()) {
            return ResponseHandler.errorResponseBuilder(
                "User tidak dapat ditemukan!",
                HttpStatus.NOT_FOUND
            );
        }
        UsersEntity usersCur = getUsers.get();
        try {
            usersCur.setFullname(data.getFullname());
            usersCur.setAddress(data.getAddress());
            usersCur.setPhones(data.getPhones());

            Set<RolesEntity> roles = new HashSet<>();
            if (data.getRole() != null) {
                data.getRole().forEach(role -> {
                    RolesEntity roleEntity = rolesRepository.findByName(ERole.valueOf(role))
                            .orElseThrow(() -> new RuntimeException("Error: Role " + role + " is not found."));
                    roles.add(roleEntity);
                });
            }
            usersCur.setRoles(roles);

            Set<DivisionEntity> divisions = new HashSet<>();
            if (data.getDivision() != null) {
                data.getDivision().forEach(division -> {
                    DivisionEntity divisionEntity = divisionRepository.findByName(EDivision.valueOf(division))
                            .orElseThrow(() -> new RuntimeException("Error: Division " + division + " is not found."));
                    divisions.add(divisionEntity);
                });
            }
            usersCur.setDivision(divisions);

            UsersEntity savedUser = usersRepository.save(usersCur);
            
            Map<String, Object> responseData = new TreeMap<>();
            responseData.put("id", savedUser.getId());
            responseData.put("fullname", savedUser.getFullname());
            responseData.put("address", savedUser.getAddress());
            responseData.put("phones", savedUser.getPhones());
            responseData.put("roles", savedUser.getRoles().stream().map(RolesEntity::getName).collect(Collectors.toList()));
            responseData.put("divisions", savedUser.getDivision().stream().map(DivisionEntity::getName).collect(Collectors.toList()));
            responseData.put("created_at", savedUser.getCreatedAt());
            responseData.put("updated_at", savedUser.getUpdatedAt());

            return ResponseHandler.successResponseBuilder(
                "Data user berhasil diperbarui!",
                HttpStatus.OK,
                responseData
            );
        } catch (DataAccessException e) {
            return ResponseHandler.errorResponseBuilder(
                "Data tidak dapat diperbarui!",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<Object> deleteUser(UUID id) {
        try {
            Optional<UsersEntity> existUser = usersRepository.findById(id);
            if (existUser.isPresent()) {
                UsersEntity existingUser = existUser.get();
                existingUser.getRoles().size();
                existingUser.getDivision().size();
                
                usersRepository.deleteById(id);

                Map<String, Object> responseData = new TreeMap<>();
                responseData.put("id", existingUser.getId());
                responseData.put("fullname", existingUser.getFullname());
                responseData.put("address", existingUser.getAddress());
                responseData.put("phones", existingUser.getPhones());
                responseData.put("roles", existingUser.getRoles().stream().map(RolesEntity::getName).collect(Collectors.toList()));
                responseData.put("divisions", existingUser.getDivision().stream().map(DivisionEntity::getName).collect(Collectors.toList()));
                responseData.put("created_at", existingUser.getCreatedAt());
                responseData.put("updated_at", existingUser.getUpdatedAt());

                return ResponseHandler.successResponseBuilder(
                    "Data user berhasil untuk dihapus!",
                    HttpStatus.OK,
                    responseData
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                    "Data user tidak dapat ditemukan!",
                    HttpStatus.NOT_FOUND
                );
            }
        } catch (DataAccessException e) {
            return ResponseHandler.errorResponseBuilder(
                "Data tidak dapat dihapus!",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<Object> getDetails(UUID id) {
        try {
            Optional<UsersEntity> userOptional = usersRepository.findById(id);
            if (userOptional.isPresent()) {
                UsersEntity users = userOptional.get();

                return ResponseHandler.successResponseBuilder(
                    "Berhasil medapatkan data user!.",
                    HttpStatus.OK,
                    users
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "User idak dapat ditemukan!.",
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak dapat ditemukan!" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<UsersEntity> getAll = usersRepository.findAll();
            return ResponseHandler.successResponseBuilder(
                "Berhasil mendapatkan semua data user!.",
                HttpStatus.OK,
                getAll
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak dapat ditemukan" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getListing(int page, int size, String name, String nik, String role, String division) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<UsersEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (name != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullname")), "%" + name.toLowerCase() + "%"));
            }
            if (nik != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nik")), "%" + nik.toLowerCase() + "%"));
            }
            if (role != null) {
                Join<UsersEntity, RolesEntity> rolesJoin = root.join("roles", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(rolesJoin.get("name")), "%" + role.toLowerCase() + "%"));
            }
            if (division != null) {
                Join<UsersEntity, DivisionEntity> divisionJoin = root.join("division", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(divisionJoin.get("name")), "%" + division.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        try {
            Page<UsersEntity> usersPage = usersRepository.findAll(specification, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("data", usersPage.getContent());
            response.put("totalItems", usersPage.getTotalElements());
            response.put("totalPages", usersPage.getTotalPages());
            response.put("currentPage", usersPage.getNumber() + 1);

            return ResponseEntity.ok().body(response);
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak dapat ditemukan: " + e.getMessage());
        }
    }


}
