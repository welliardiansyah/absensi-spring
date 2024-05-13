package com.absensi.absensi.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.absensi.absensi.database.entities.AbsensiEntity;
import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.EAbsensi;
import com.absensi.absensi.database.entities.RolesEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.AbsensiRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.AbsensiRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.services.AbsensiService;

@Service
public class AbsensiServiceImpl implements AbsensiService{
    @Autowired
    private AbsensiRepository absensiRepository;


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
    public ResponseEntity<Object> updateAbsensi(AbsensiRequest data) {
        if (!absensiRepository.existsById(data.getid())) {
            return ResponseHandler.errorResponseBuilder(
                    "Data absensi tidak dapat ditemukan.",
                    HttpStatus.NOT_FOUND
            );
        }
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
                data.getid(), 
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
                "Data absensi berhasil diubah!.",
                HttpStatus.OK,
                saveData
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> deleteAbsensi(UUID id) {
        try {
            Optional<AbsensiEntity> existAbsen = absensiRepository.findById(id);
            if (!existAbsen.isPresent()) {
                    absensiRepository.deleteById(id);
                    return ResponseHandler.errorResponseBuilder(
                        "Data absensi tidak dapat ditemukan!.",
                        HttpStatus.NOT_FOUND
                );
            } else {
                absensiRepository.deleteById(id);
                return ResponseHandler.successResponseBuilder(
                    "Data absensi berhasil dihapus.",
                    HttpStatus.OK,
                    id
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Data tidak ditemukan: " + e.getMessage());
        }
    }


    @Override
    public ResponseEntity<Object> getDetails(UUID id) {
        try {
            Optional<AbsensiEntity> absensiOptional = absensiRepository.findById(id);
            if (absensiOptional.isPresent()) {
                AbsensiEntity absensi = absensiOptional.get();
                UsersEntity user = absensi.getUsers();

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", absensi.getId());
                responseData.put("types", absensi.getTypes());
                responseData.put("time", absensi.getTime());
                responseData.put("time_start", absensi.getTime_start());
                responseData.put("time_end", absensi.getTime_end());
                responseData.put("latitude", absensi.getLatitude());
                responseData.put("longitude", absensi.getLongitude());
                responseData.put("description", absensi.getDescription());
                responseData.put("createdAt", absensi.getCreatedAt());
                responseData.put("updatedAt", absensi.getUpdatedAt());

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

                List<Map<String, Object>> divisionsData = new ArrayList<>();
                for (DivisionEntity division : user.getDivision()) {
                    Map<String, Object> divisionData = new HashMap<>();
                    divisionData.put("id", division.getId());
                    divisionData.put("name", division.getName());
                    divisionData.put("is_actived", division.getIs_actived());
                    divisionData.put("time_start", division.getTime_start());
                    divisionData.put("time_end", division.getTime_end());
                    divisionData.put("createdAt", division.getCreatedAt());
                    divisionData.put("updatedAt", division.getUpdatedAt());
                    divisionsData.add(divisionData);
                }
                userData.put("division", divisionsData);

                List<Map<String, Object>> rolesData = new ArrayList<>();
                for (RolesEntity role : user.getRoles()) {
                    Map<String, Object> roleData = new HashMap<>();
                    roleData.put("id", role.getId());
                    roleData.put("name", role.getName());
                    roleData.put("is_actived", role.getIs_actived());
                    roleData.put("createdAt", role.getCreatedAt());
                    roleData.put("updatedAt", role.getUpdatedAt());
                    rolesData.add(roleData);
                }
                userData.put("roles", rolesData);
                responseData.put("users", userData);

                return ResponseHandler.successResponseBuilder(
                        "Get details absensi successfully!",
                        HttpStatus.OK,
                        responseData
                );
            } else {
                return ResponseHandler.errorResponseBuilder(
                        "Absensi not found!",
                        HttpStatus.BAD_REQUEST
                );
            }
        } catch (DataAccessException e) {
            throw new NotFoundException("Data not found! " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<AbsensiEntity> absensiList = absensiRepository.findAll();

            List<Map<String, Object>> responseDataList = new ArrayList<>();
            for (AbsensiEntity absensi : absensiList) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", absensi.getId());
                responseData.put("types", absensi.getTypes());
                responseData.put("time", absensi.getTime());
                responseData.put("time_start", absensi.getTime_start());
                responseData.put("time_end", absensi.getTime_end());
                responseData.put("latitude", absensi.getLatitude());
                responseData.put("longitude", absensi.getLongitude());
                responseData.put("description", absensi.getDescription());
                responseData.put("createdAt", absensi.getCreatedAt());
                responseData.put("updatedAt", absensi.getUpdatedAt());

                UsersEntity user = absensi.getUsers();

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

                List<Map<String, Object>> divisionsData = new ArrayList<>();
                for (DivisionEntity division : user.getDivision()) {
                    Map<String, Object> divisionData = new HashMap<>();
                    divisionData.put("id", division.getId());
                    divisionData.put("name", division.getName());
                    divisionData.put("is_actived", division.getIs_actived());
                    divisionData.put("time_start", division.getTime_start());
                    divisionData.put("time_end", division.getTime_end());
                    divisionData.put("createdAt", division.getCreatedAt());
                    divisionData.put("updatedAt", division.getUpdatedAt());
                    divisionsData.add(divisionData);
                }
                userData.put("division", divisionsData);

                List<Map<String, Object>> rolesData = new ArrayList<>();
                for (RolesEntity role : user.getRoles()) {
                    Map<String, Object> roleData = new HashMap<>();
                    roleData.put("id", role.getId());
                    roleData.put("name", role.getName());
                    roleData.put("is_actived", role.getIs_actived());
                    roleData.put("createdAt", role.getCreatedAt());
                    roleData.put("updatedAt", role.getUpdatedAt());
                    rolesData.add(roleData);
                }
                userData.put("roles", rolesData);
                responseData.put("users", userData);
                responseDataList.add(responseData);
            }

            return ResponseHandler.successResponseBuilder(
                    "Get all absensi successfully!",
                    HttpStatus.OK,
                    responseDataList
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Data not found! " + e.getMessage());
        }
    }

    public ResponseEntity<Object> getListing(int page, int size, UUID users, EAbsensi types) {
        try {
            UsersEntity usersEntity = null;
            if (users != null) {
                usersEntity = usersRepository.findById(users).orElse(null);
            }

            Pageable paging;
            if (users == null && types == null) {
                paging = PageRequest.of(page, size);
            } else if (users == null) {
                paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "types"));
            } else if (types == null) {
                paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "users"));
            } else {
                paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "types", "users"));
            }

            Page<AbsensiEntity> absensiPage;
            if (users != null && types != null) {
                absensiPage = absensiRepository.findByUsersAndTypes(usersEntity, types, paging);
            } else if (users != null) {
                absensiPage = absensiRepository.findByUsers(usersEntity, paging);
            } else if (types != null) {
                absensiPage = absensiRepository.findByTypes(types, paging);
            } else {
                absensiPage = absensiRepository.findAll(paging);
            }

            List<Map<String, Object>> responseDataList = new ArrayList<>();
            for (AbsensiEntity absensi : absensiPage.getContent()) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", absensi.getId());
                responseData.put("types", absensi.getTypes());
                responseData.put("time", absensi.getTime());
                responseData.put("time_start", absensi.getTime_start());
                responseData.put("time_end", absensi.getTime_end());
                responseData.put("latitude", absensi.getLatitude());
                responseData.put("longitude", absensi.getLongitude());
                responseData.put("description", absensi.getDescription());
                responseData.put("createdAt", absensi.getCreatedAt());
                responseData.put("updatedAt", absensi.getUpdatedAt());
    
                UsersEntity user = absensi.getUsers();
    
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
    
                List<Map<String, Object>> divisionsData = new ArrayList<>();
                for (DivisionEntity division : user.getDivision()) {
                    Map<String, Object> divisionData = new HashMap<>();
                    divisionData.put("id", division.getId());
                    divisionData.put("name", division.getName());
                    divisionData.put("is_actived", division.getIs_actived());
                    divisionData.put("time_start", division.getTime_start());
                    divisionData.put("time_end", division.getTime_end());
                    divisionData.put("createdAt", division.getCreatedAt());
                    divisionData.put("updatedAt", division.getUpdatedAt());
                    divisionsData.add(divisionData);
                }
                userData.put("division", divisionsData);
    
                List<Map<String, Object>> rolesData = new ArrayList<>();
                for (RolesEntity role : user.getRoles()) {
                    Map<String, Object> roleData = new HashMap<>();
                    roleData.put("id", role.getId());
                    roleData.put("name", role.getName());
                    roleData.put("is_actived", role.getIs_actived());
                    roleData.put("createdAt", role.getCreatedAt());
                    roleData.put("updatedAt", role.getUpdatedAt());
                    rolesData.add(roleData);
                }
                userData.put("roles", rolesData);
                responseData.put("users", userData);
                responseDataList.add(responseData);
            }
            
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("totalItems", absensiPage.getTotalElements());
            responseMap.put("totalPages", absensiPage.getTotalPages());
            responseMap.put("currentPage", absensiPage.getNumber() + 1);
            responseMap.put("data", responseDataList);
    
            return ResponseHandler.successResponseBuilder("Berhasil mendapatkan list absensi", HttpStatus.OK, responseMap);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving absensi data: " + e.getMessage());
        }
    }
}
