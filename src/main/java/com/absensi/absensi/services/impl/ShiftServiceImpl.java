package com.absensi.absensi.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;

import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.ShiftEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.DivisionRepository;
import com.absensi.absensi.database.repository.ShiftRepository;
import com.absensi.absensi.dto.ShiftRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.ShiftService;

import jakarta.persistence.criteria.Predicate;

@Service
public class ShiftServiceImpl implements ShiftService {
    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    DivisionRepository divisionRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository, DivisionRepository divisionRepository) {
        this.shiftRepository = shiftRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public ResponseEntity<Object> createShift(ShiftRequest data) {
        DivisionEntity divisionId = null;
        if (data.getDivisions() != null) {
            divisionId = divisionRepository.findById(data.getDivisions()).orElse(null);
        }
        try {
            ShiftEntity saveData = new ShiftEntity();
            saveData.setName(data.getName());
            saveData.setIs_actived(data.getIs_actived());
            saveData.setTime_start(data.getTime_start());
            saveData.setTime_end(data.getTime_end());
            saveData.setDivisions(divisionId);

            ShiftEntity insertData = shiftRepository.save(saveData);
            return ResponseHandler.successResponseBuilder(
                "Shift berhasil dibuat!",
                HttpStatus.OK,
                insertData
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak ditemukan: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateShift(UUID id, ShiftRequest data) {
        Optional<ShiftEntity> checkShift = shiftRepository.findById(id);
        if (checkShift.isEmpty()) {
            return ResponseHandler.errorResponseBuilder(
                "Data shift tidak dapat ditemukan.",
                HttpStatus.NOT_FOUND
            );
        }
        try {
            ShiftEntity shiftData = checkShift.get();
            shiftData.setName(data.getName());
            shiftData.setIs_actived(data.getIs_actived());
            shiftData.setTime_start(data.getTime_start());
            shiftData.setTime_end(data.getTime_end());
    
            updateReference(data.getDivisions(), shiftData::setDivisions, "User superior");
            ShiftEntity savedShift = shiftRepository.save(shiftData);
            return ResponseHandler.successResponseBuilder(
                "Shift berhasil diperbarui!",
                HttpStatus.OK,
                savedShift
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak dapat ditemukan: " + e.getMessage());
        }
    }
    
    private void updateReference(UUID divisionId, Consumer<DivisionEntity> setter, String userType) {
        if (divisionId != null) {
            DivisionEntity division = divisionRepository.findById(divisionId)
                .orElseThrow(() -> new NotFoundException(userType + " data tidak dapat ditemukan."));
            setter.accept(division);
        } else {
            setter.accept(null);
        }
    }
    

    @Override
    public ResponseEntity<Object> deleteShift(UUID id) {
        Optional<ShiftEntity> existShift = shiftRepository.findById(id);
            if (!existShift.isPresent()) {
                return ResponseHandler.errorResponseBuilder(
                    "Data shift tidak dapat ditemukan!.",
                    HttpStatus.NOT_FOUND
                );
            } else {
                ShiftEntity shiftData = existShift.get();
                
                shiftRepository.deleteById(id);
                return ResponseHandler.successResponseBuilder(
                    "Data shift berhasil dihapus.",
                    HttpStatus.OK,
                    shiftData
                );
            }
    }

    @Override
    public ResponseEntity<Object> getDetails(UUID id) {
        try {
            Optional<ShiftEntity> shiftResponse = shiftRepository.findById(id);
            if (shiftResponse.isPresent()) {
                ShiftEntity shiftData = shiftResponse.get();

                return ResponseHandler.successResponseBuilder(
                        "Get details shift successfully!",
                        HttpStatus.OK,
                        shiftData
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "Data shift tidak ditemukan!",
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak ditemukan: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getAll() {
        List<ShiftEntity> absensiList = shiftRepository.findAll();
        try {
            return ResponseHandler.successResponseBuilder(
                "Get all absensi successfully!",
                HttpStatus.OK,
                absensiList
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak ditemukan: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getListing(int page, int size, String search, UUID division) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<ShiftEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), search));
            }
            if (division != null) {
                predicates.add(criteriaBuilder.equal(root.get("divisions").get("id"), division));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ShiftEntity> shiftPage = shiftRepository.findAll(specification, pageable);
        try {
            List<Map<String, Object>> responseDataList = new ArrayList<>();
            for (ShiftEntity shift : shiftPage.getContent()) {
                Map<String, Object> responseData = new TreeMap<>();
                responseData.put("id", shift.getId());
                responseData.put("name", shift.getName());
                responseData.put("is_active", shift.getIs_actived());
                responseData.put("time_start", shift.getTime_start());
                responseData.put("time_end", shift.getTime_end());

                DivisionEntity divisionEntity = shift.getDivisions();
                if (divisionEntity != null) {
                    responseData.put("divisions", getDivisionDataMap(divisionEntity));
                }

                responseData.put("created_at", shift.getCreatedAt());
                responseData.put("updated_at", shift.getUpdatedAt());

                responseDataList.add(responseData);
            }

            Map<String, Object> responseMap = new TreeMap<>();
            responseMap.put("totalItems", shiftPage.getTotalElements());
            responseMap.put("totalPages", shiftPage.getTotalPages());
            responseMap.put("currentPage", shiftPage.getNumber() + 1);
            responseMap.put("data", responseDataList);

            return ResponseHandler.successResponseBuilder("Berhasil mendapatkan data shifts", HttpStatus.OK, responseMap);
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak ditemukan: " + e.getMessage());
        }
    }

    private Map<String, Object> getDivisionDataMap(DivisionEntity division) {
        Map<String, Object> divisionData = new TreeMap<>();
        divisionData.put("id", division.getId());
        divisionData.put("name", division.getName());
        divisionData.put("is_actived", division.getIs_actived());
        divisionData.put("createdAt", division.getCreatedAt());
        divisionData.put("updatedAt", division.getUpdatedAt());
        return divisionData;
    }

    @Override
    public ResponseEntity<Object> getShiftsByDivisionId(UUID id) {
        try {
            List<ShiftEntity> shifts = shiftRepository.findByDivisions_Id(id);
            if (!shifts.isEmpty()) {
                return ResponseHandler.successResponseBuilder("Data shift division berhasil didapat!", HttpStatus.OK, shifts);
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "Data shift tidak ditemukan!",
                        HttpStatus.NOT_FOUND
                );
            }
        } catch (DataAccessException e) {
            throw new NoTransactionException("Data tidak ditemukan!" + e.getMessage());
        }
    }    
}
