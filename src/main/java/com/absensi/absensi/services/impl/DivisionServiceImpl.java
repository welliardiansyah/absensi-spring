package com.absensi.absensi.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.absensi.absensi.database.entities.AbsensiEntity;
import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.EDivision;
import com.absensi.absensi.database.entities.RolesEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.DivisionRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.DivisionRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.DivisionService;

import jakarta.persistence.criteria.Predicate;

@Service
public class DivisionServiceImpl implements DivisionService {
    @Autowired
    DivisionRepository divisionRepository;

    @Autowired
    UsersRepository usersRepository;

    public DivisionServiceImpl(DivisionRepository divisionRepository, UsersRepository usersRepository) {
        this.divisionRepository = divisionRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public ResponseEntity<Object> createDivision(DivisionRequest data) {
        Optional<DivisionEntity> existName = divisionRepository.findByName(data.getName());
        if (existName.isPresent()) {
            return ResponseHandler.errorResponseBuilder(
                "Nama division ini sudah digunakan!.",
                HttpStatus.BAD_REQUEST
            );
        }
        try {
            UsersEntity superiorId = null;
            UsersEntity managerId = null;
            UsersEntity leaderId = null;

            if (data.getSuperior() != null) {
                superiorId = usersRepository.findById(data.getSuperior()).orElse(null);
            }

            if (data.getManager() != null) {
                managerId = usersRepository.findById(data.getSuperior()).orElse(null);
            }

            if (data.getLeader() != null) {
                leaderId = usersRepository.findById(data.getSuperior()).orElse(null);
            }

            DivisionEntity division = new DivisionEntity();
            division.setName(data.getName());
            division.setIs_actived(data.getIs_actived());
            division.setTime_start(data.getTime_start());
            division.setTime_end(data.getTime_end());
            division.setSuperior(superiorId);
            division.setManager(managerId);
            division.setLeader(leaderId);

            DivisionEntity saveData = divisionRepository.save(division);
            return ResponseHandler.successResponseBuilder(
                "Divisi berhasil dibuat!",
                HttpStatus.OK,
                saveData
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak ditemukan! " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateDivision(DivisionRequest data) {
        Optional<DivisionEntity> divisionOptional = divisionRepository.findById(data.getId());
        if (!divisionOptional.isPresent()) {
            return ResponseHandler.errorResponseBuilder(
                "Data division tidak dapat ditemukan.",
                HttpStatus.NOT_FOUND
            );
        }
        DivisionEntity division = divisionOptional.get();

        division.setName(data.getName());
        division.setIs_actived(data.getIs_actived());
        division.setTime_start(data.getTime_start());
        division.setTime_end(data.getTime_end());

        updateReference(data.getSuperior(), division::setSuperior, "User superior");
        updateReference(data.getManager(), division::setManager, "User manager");
        updateReference(data.getLeader(), division::setLeader, "User leader");

        try {
            DivisionEntity savedDivision = divisionRepository.save(division);
            return ResponseHandler.successResponseBuilder(
                "Divisi berhasil diperbarui!",
                HttpStatus.OK,
                savedDivision
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak dapat disimpan! " + e.getMessage());
        }
    }

    private void updateReference(UUID userId, Consumer<UsersEntity> setter, String userType) {
        if (userId != null) {
            UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userType + " tidak ditemukan."));
            setter.accept(user);
        } else {
            setter.accept(null);
        }
    }

    @Override
    public ResponseEntity<Object> deleteDivision(UUID id) {
        try {
            Optional<DivisionEntity> existingDivision = divisionRepository.findById(id);
            if (!existingDivision.isPresent()) {
                return ResponseHandler.errorResponseBuilder(
                    "Data division tidak dapat ditemukan!.",
                    HttpStatus.NOT_FOUND
                );
            } else {
                DivisionEntity division = existingDivision.get();
                List<UsersEntity> usersReferencingDivision = usersRepository.findByDivision(division);
                if (!usersReferencingDivision.isEmpty()) {
                    for (UsersEntity user : usersReferencingDivision) {
                        user.setDivision(null);
                        usersRepository.save(user);
                    }
                }
                divisionRepository.deleteById(id);
                return ResponseHandler.successResponseBuilder(
                    "Data division berhasil dihapus.",
                    HttpStatus.OK,
                    division
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak ditemukan!" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getDetails(UUID id) {
        try {
            Optional<DivisionEntity> divisi = divisionRepository.findById(id);
            if (divisi.isPresent()) {
                DivisionEntity divisions = divisi.get();

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", divisions.getId());
                responseData.put("name", divisions.getName());
                responseData.put("is_active", divisions.getIs_actived());
                responseData.put("time_start", divisions.getTime_start());
                responseData.put("time_end", divisions.getTime_end());
                responseData.put("created_at", divisions.getCreatedAt());
                responseData.put("updated_at", divisions.getUpdatedAt());

                UsersEntity superior = divisions.getSuperior();
                if (superior != null) {
                    responseData.put("superior", getUserDataMap(superior));
                }

                UsersEntity manager = divisions.getManager();
                if (manager != null) {
                    responseData.put("manager", getUserDataMap(manager));
                }

                UsersEntity leader = divisions.getLeader();
                if (leader != null) {
                    responseData.put("leader", getUserDataMap(leader));
                }

                return ResponseHandler.successResponseBuilder(
                    "Get details division successfully!.",
                    HttpStatus.OK,
                    responseData
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                    "Division not found!.",
                    HttpStatus.NOT_FOUND
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    private Map<String, Object> getUserDataMap(UsersEntity user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("fullname", user.getFullname());
        userData.put("nik", user.getNik());
        userData.put("address", user.getAddress());
        userData.put("phones", user.getPhones());
        userData.put("email", user.getEmail());
        userData.put("is_actived", user.getIs_actived());
        userData.put("createdAt", user.getCreatedAt());
        userData.put("updatedAt", user.getUpdatedAt());
        return userData;
    }

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<DivisionEntity> divisionsList = divisionRepository.findAll();
            if (!divisionsList.isEmpty()) {
                List<Map<String, Object>> responseDataList = new ArrayList<>();

                for (DivisionEntity divisions : divisionsList) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("id", divisions.getId());
                    responseData.put("name", divisions.getName());
                    responseData.put("is_active", divisions.getIs_actived());
                    responseData.put("time_start", divisions.getTime_start());
                    responseData.put("time_end", divisions.getTime_end());
                    responseData.put("created_at", divisions.getCreatedAt());
                    responseData.put("updated_at", divisions.getUpdatedAt());

                    UsersEntity superior = divisions.getSuperior();
                    if (superior != null) {
                        responseData.put("superior", getUserDataMap(superior));
                    }

                    UsersEntity manager = divisions.getManager();
                    if (manager != null) {
                        responseData.put("manager", getUserDataMap(manager));
                    }

                    UsersEntity leader = divisions.getLeader();
                    if (leader != null) {
                        responseData.put("leader", getUserDataMap(leader));
                    }

                    responseDataList.add(responseData);
                }

                return ResponseHandler.successResponseBuilder(
                    "Get all divisions successfully!.",
                    HttpStatus.OK,
                    responseDataList
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                    "Divisions not found!.",
                    HttpStatus.NOT_FOUND
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }


    @Override
    public ResponseEntity<Object> getListing(int page, int size, EDivision search, UUID superior, UUID manager, UUID leader) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            Specification<DivisionEntity> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (search != null) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), search));
                }
                if (superior != null) {
                    predicates.add(criteriaBuilder.equal(root.get("superior").get("id"), superior));
                }
                if (manager != null) {
                    predicates.add(criteriaBuilder.equal(root.get("manager").get("id"), manager));
                }
                if (leader != null) {
                    predicates.add(criteriaBuilder.equal(root.get("leader").get("id"), leader));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<DivisionEntity> divisionsPage = divisionRepository.findAll(specification, pageable);

            List<Map<String, Object>> responseDataList = new ArrayList<>();
            for (DivisionEntity division : divisionsPage.getContent()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", division.getId());
                responseData.put("name", division.getName());
                responseData.put("is_active", division.getIs_actived());
                responseData.put("time_start", division.getTime_start());
                responseData.put("time_end", division.getTime_end());
                responseData.put("created_at", division.getCreatedAt());
                responseData.put("updated_at", division.getUpdatedAt());

                UsersEntity superiorUser = division.getSuperior();
                if (superiorUser != null) {
                    responseData.put("superior", getUserDataMap(superiorUser));
                }

                UsersEntity managerUser = division.getManager();
                if (managerUser != null) {
                    responseData.put("manager", getUserDataMap(managerUser));
                }

                UsersEntity leaderUser = division.getLeader();
                if (leaderUser != null) {
                    responseData.put("leader", getUserDataMap(leaderUser));
                }

                responseDataList.add(responseData);
            }

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalItems", divisionsPage.getTotalElements());
            responseMap.put("totalPages", divisionsPage.getTotalPages());
            responseMap.put("currentPage", divisionsPage.getNumber() + 1);
            responseMap.put("data", responseDataList);

            return ResponseHandler.successResponseBuilder("Berhasil mendapatkan data divisions", HttpStatus.OK, responseMap);
        } catch (DataAccessException e) {
            throw new NotFoundException("Data not found!." + e.getMessage());
        }
    }
}
