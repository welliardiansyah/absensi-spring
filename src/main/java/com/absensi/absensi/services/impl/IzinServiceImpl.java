package com.absensi.absensi.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.absensi.absensi.database.entities.IzinEntity;
import com.absensi.absensi.database.entities.EIzin;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.IzinRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.IzinRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.IzinService;

import jakarta.persistence.criteria.Predicate;

import org.springframework.util.StringUtils;

@Service
public class IzinServiceImpl implements IzinService{
    @Autowired
    IzinRepository izinRepository;

    @Autowired
    UsersRepository usersRepository;

    public IzinServiceImpl(IzinRepository izinRepository, UsersRepository usersRepository) {
        this.izinRepository = izinRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public ResponseEntity<Object> createIzin(IzinRequest data) {
        if (data == null) {
            return ResponseEntity.badRequest().body("Data is null");
        }

        if (data.getUsers() == null) {
            return ResponseEntity.badRequest().body("User ID is null");
        }

        Optional<UsersEntity> optionalUser = usersRepository.findById(data.getUsers());
        if (optionalUser.isEmpty()) {
            return ResponseHandler.errorResponseBuilder(
                    "User tidak ditemukan!",
                    HttpStatus.NOT_FOUND
            );
        }
        UsersEntity user = optionalUser.get();

        try {
            MultipartFile file = data.getPhoto();
            String fileName = (file != null) ? StringUtils.cleanPath(file.getOriginalFilename()) : null;
            String fileUrl = null;

            if (file != null && !file.isEmpty()) {
                String uploadDir = "./uploads/";
                File uploadDirectory = new File(uploadDir);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, file.getBytes());
                fileUrl = filePath.toString();
            }

            IzinEntity izinData = new IzinEntity(
                null,
                data.getName(),
                data.getDateStart(),
                data.getDateEnd(),
                data.getDescription(),
                fileName,
                user,
                data.getIs_actived(),
                (file != null) ? file.getContentType() : null,
                (file != null) ? file.getBytes() : null,
                fileUrl,
                LocalDateTime.now(),
                LocalDateTime.now()
            );

            IzinEntity savedData = izinRepository.save(izinData);
            savedData.setBytes(null);
            savedData.getUsers().setPassword(null);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Izin berhasil dibuat!");
            response.put("created_data", savedData);
            response.put("photo_url", savedData.getPhotoUrl());

            String streamImageUrl = "/api/v1/image/" + fileName;
            response.put("stream_image_url", streamImageUrl);

            return ResponseHandler.successResponseBuilder(
                "Data izin berhasil dibuat!",
                HttpStatus.OK,
                response
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Internal server error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateIzin(UUID id, IzinRequest data) {
        if (!izinRepository.existsById(id)) {
            return ResponseHandler.errorResponseBuilder(
                    "Data izin tidak dapat ditemukan.",
                    HttpStatus.NOT_FOUND
            );
        }
        Optional<UsersEntity> optionalUser = usersRepository.findById(data.getUsers());
        if (optionalUser.isEmpty()) {
            return ResponseHandler.errorResponseBuilder(
                    "User tidak ditemukan!",
                    HttpStatus.NOT_FOUND
            );
        }
        UsersEntity user = optionalUser.get();
        try {
            MultipartFile file = data.getPhoto();
            String fileName = (file != null) ? StringUtils.cleanPath(file.getOriginalFilename()) : null;
            byte[] fileBytes = (file != null) ? file.getBytes() : null;
            String fileType = (file != null) ? file.getContentType() : null;
            String fileUrl = null;

            if (file != null && !file.isEmpty()) {
                String uploadDir = "./uploads/";
                File uploadDirectory = new File(uploadDir);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, file.getBytes());
                fileUrl = filePath.toString();
            }

            IzinEntity izinData = new IzinEntity(
                id,
                data.getName(),
                data.getDateStart(),
                data.getDateEnd(),
                data.getDescription(),
                fileName,
                user,
                data.getIs_actived(),
                (fileType != null) ? fileType : null,
                (fileBytes != null) ? fileBytes : null,
                fileUrl,
                LocalDateTime.now(),
                LocalDateTime.now()
            );

            IzinEntity savedData = izinRepository.save(izinData);
            savedData.setBytes(null);
            savedData.getUsers().setPassword(null);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Izin berhasil dibuat!");
            response.put("created_data", savedData);
            response.put("photo_url", savedData.getPhotoUrl());

            String streamImageUrl = "/api/v1/image/" + fileName;
            response.put("stream_image_url", streamImageUrl);

            return ResponseHandler.successResponseBuilder(
                "Data izin berhasil diperbarui!",
                HttpStatus.OK,
                response
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Internal server error" + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deleteIzin(UUID id) {
        try {
            Optional<IzinEntity> existAbsen = izinRepository.findById(id);
            if (!existAbsen.isPresent()) {
                izinRepository.deleteById(id);
                    return ResponseHandler.errorResponseBuilder(
                        "Data izin tidak dapat ditemukan!.",
                        HttpStatus.NOT_FOUND
                );
            } else {
                izinRepository.deleteById(id);
                return ResponseHandler.successResponseBuilder(
                    "Data izin berhasil dihapus.",
                    HttpStatus.OK,
                    id
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Internal server error" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getDetails(UUID id) {
        Optional<IzinEntity> detailIzin = izinRepository.findById(id);
        try {
            if (detailIzin.isPresent()) {
                IzinEntity details = detailIzin.get();
                details.setBytes(null);
                details.getUsers().setPassword(null);

                String file = details.getPhoto();
                String fileName = (file != null) ? StringUtils.cleanPath(file) : null;


                Map<String, Object> response = new HashMap<>();
                response.put("created_data", details);
                String streamImageUrl = "/api/v1/image/" + fileName;
                response.put("stream_image_url", streamImageUrl);
                
                return ResponseHandler.successResponseBuilder(
                    "Detail izin berhasil dimuat!",
                    HttpStatus.OK,
                    response
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Internal server error" + e.getMessage());
        }
        return null;
    }

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<IzinEntity> resultData = izinRepository.findAll();
            List<Map<String, Object>> responseData = new ArrayList<>();

            for (IzinEntity item : resultData) {
                item.setBytes(null);

                String file = item.getPhoto();
                String fileName = (file != null) ? StringUtils.cleanPath(file) : null;

                Map<String, Object> responseItem = new HashMap<>();
                responseItem.put("created_data", item);
                String streamImageUrl = "/api/v1/image/" + fileName;
                responseItem.put("stream_image_url", streamImageUrl);

                responseData.add(responseItem);
            }

            return ResponseHandler.successResponseBuilder(
                "Get all absensi successfully!",
                HttpStatus.OK,
                responseData
            );
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found", e);
        }
    }

    @Override
    public ResponseEntity<Object> getListing(int page, int size, EIzin name, UUID users) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            Specification<IzinEntity> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), name));
                }
                if (users != null) {
                    predicates.add(criteriaBuilder.equal(root.get("users").get("id"), users));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<IzinEntity> izinPage = izinRepository.findAll(specification, pageable);

            List<Map<String, Object>> responseDataList = new ArrayList<>();
            for (IzinEntity izin : izinPage.getContent()) {
                Map<String, Object> responseData = new TreeMap<>();
                responseData.put("id", izin.getId());
                responseData.put("name", izin.getName());
                responseData.put("date_start", izin.getDateStart());
                responseData.put("date_end", izin.getDateEnd());
                responseData.put("description", izin.getDescription());
                responseData.put("photo", izin.getPhoto());
                responseData.put("users", izin.getUsers());
                responseData.put("is_active", izin.getIs_actived());
                responseData.put("created_at", izin.getCreatedAt());
                responseData.put("updated_at", izin.getUpdatedAt());

                String file = izin.getPhoto();
                String fileName = (file != null) ? StringUtils.cleanPath(file) : null;

                Map<String, Object> responseItem = new HashMap<>();
                responseItem.put("created_data", responseData);
                String streamImageUrl = "/api/v1/image/" + fileName;
                responseItem.put("stream_image_url", streamImageUrl);

                responseDataList.add(responseItem);
            }

            Map<String, Object> responseMap = new TreeMap<>();
        responseMap.put("totalItems", izinPage.getTotalElements());
        responseMap.put("totalPages", izinPage.getTotalPages());
        responseMap.put("currentPage", izinPage.getNumber() + 1);
        responseMap.put("data", responseDataList);

        return ResponseHandler.successResponseBuilder("Berhasil mendapatkan data divisions", HttpStatus.OK, responseMap);
    } catch (DataAccessException e) {
        throw new NotFoundException("Data not found!." + e.getMessage());
    }
}

}
