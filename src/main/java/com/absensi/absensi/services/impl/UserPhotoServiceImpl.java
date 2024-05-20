package com.absensi.absensi.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.absensi.absensi.database.entities.UserPhotoEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.UserPhotoRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.UserPhotoRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.UserPhotoService;

@Service
public class UserPhotoServiceImpl implements UserPhotoService {
    @Autowired
    UserPhotoRepository userPhotoRepository;

    @Autowired
    UsersRepository usersRepository;

    public UserPhotoServiceImpl(UserPhotoRepository userPhotoRepository, UsersRepository usersRepository) {
        this.userPhotoRepository = userPhotoRepository;
        this.usersRepository = usersRepository;
    }

     @Override
    public ResponseEntity<Object> createUserPhoto(UserPhotoRequest data) {
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

            UserPhotoEntity izinData = new UserPhotoEntity(
                null,
                fileName,
                (file != null) ? file.getContentType() : null,
                (file != null) ? file.getBytes() : null,
                fileUrl,
                user,
                null, 
                null
            );

            UserPhotoEntity savedData = userPhotoRepository.save(izinData);
            savedData.setBytes(null);
            savedData.getUsers().setPassword(null);

            Map<String, Object> response = new HashMap<>();
            response.put("photo_url", savedData.getPhotoUrl());
            response.put("users", savedData.getUsers());
            String streamImageUrl = "/api/v1/image/" + fileName;
            response.put("stream_image_url", streamImageUrl);

            return ResponseHandler.successResponseBuilder(
                "Data user photo berhasil dibuat!",
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
    public ResponseEntity<Object> updateUserPhoto(UUID id, UserPhotoRequest data) {
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

            UserPhotoEntity izinData = new UserPhotoEntity(
                id,
                fileName,
                (file != null) ? file.getContentType() : null,
                (file != null) ? file.getBytes() : null,
                fileUrl,
                user,
                null, 
                null
            );

            UserPhotoEntity savedData = userPhotoRepository.save(izinData);
            savedData.setBytes(null);
            savedData.getUsers().setPassword(null);

            Map<String, Object> response = new HashMap<>();
            response.put("photo_url", savedData.getPhotoUrl());
            response.put("users", savedData.getUsers());
            String streamImageUrl = "/api/v1/image/" + fileName;
            response.put("stream_image_url", streamImageUrl);

            return ResponseHandler.successResponseBuilder(
                "Data user photo berhasil diperbarui!",
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
    public ResponseEntity<Object> deleteUserPhoto(UUID id) {
        try {
            Optional<UserPhotoEntity> existAbsen = userPhotoRepository.findById(id);
            if (!existAbsen.isPresent()) {
                userPhotoRepository.deleteById(id);
                    return ResponseHandler.errorResponseBuilder(
                        "Data user tidak dapat ditemukan!.",
                        HttpStatus.NOT_FOUND
                );
            } else {
                userPhotoRepository.deleteById(id);
                return ResponseHandler.successResponseBuilder(
                    "Data user photo berhasil dihapus.",
                    HttpStatus.OK,
                    id
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Internal server error" + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Object getByUserId(UUID userId) {
        Optional<UsersEntity> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseHandler.errorResponseBuilder(
                    "User tidak ditemukan!",
                    HttpStatus.NOT_FOUND
            );
        }

        UsersEntity user = optionalUser.get();
        try {
            List<UserPhotoEntity> userPhotos = userPhotoRepository.findByUsers(user);

            Map<String, Object> response = new HashMap<>();
            response.put("photo_url", userPhotos.get(0).getPhotoUrl());
            response.put("users", userPhotos.get(0).getUsers());
            String streamImageUrl = "/api/v1/image/" + userPhotos.get(0).getPhoto();
            response.put("stream_image_url", streamImageUrl);

            return ResponseHandler.successResponseBuilder(
                "Data Photo berhasil didapatkan!",
                HttpStatus.OK,
                response
            );
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("Unable to access lob stream")) {
                return ResponseHandler.errorResponseBuilder(
                        "Gagal mengakses data LOB dari database. Silakan coba lagi nanti.",
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            } else {
                throw new NotFoundException("Internal server error: " + e.getMessage());
            }
        }
    }
}
