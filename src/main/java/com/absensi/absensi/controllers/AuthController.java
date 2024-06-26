package com.absensi.absensi.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.EDivision;
import com.absensi.absensi.database.entities.ERole;
import com.absensi.absensi.database.entities.RolesEntity;
import com.absensi.absensi.database.entities.ShiftEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.DivisionRepository;
import com.absensi.absensi.database.repository.RolesRepository;
import com.absensi.absensi.database.repository.ShiftRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.LoginRequest;
import com.absensi.absensi.dto.NikRequest;
import com.absensi.absensi.dto.RegisterRequest;
import com.absensi.absensi.exception.NotFoundException;
import com.absensi.absensi.response.ResponseHandler;
import com.absensi.absensi.security.jwt.JwtUtils;
import com.absensi.absensi.security.service.UserDetailsImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    DivisionRepository divisionRepository;

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/nik")
    public ResponseEntity<Object> loginByNik(@RequestBody NikRequest data) {
        Optional<UsersEntity> getUsers = usersRepository.findByNik(data.getNik());
        UsersEntity user = getUsers.orElseThrow(() -> new NotFoundException("User not found"));

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), data.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwtToken = jwtUtils.generateJwtToken(authentication);

            List<Map<String, Object>> divisions = user.getDivision().stream()
            .map(division -> {
                Map<String, Object> divisionMap = new HashMap<>();
                divisionMap.put("id", division.getId());
                return divisionMap;
            })
            .collect(Collectors.toList());
            Map<String, Object> firstDivision = divisions.get(0);
            UUID id = (UUID) firstDivision.get("id");
            List<ShiftEntity> shifts = shiftRepository.findByDivisions_Id(id);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            Map<String, Object> userresponse = new TreeMap<>();
            userresponse.put("id", userDetails.getId());
            userresponse.put("email", userDetails.getEmail());
            userresponse.put("fullname", userDetails.getFullname());
            userresponse.put("address", userDetails.getAddress());
            userresponse.put("nik", userDetails.getNik());
            userresponse.put("phones", userDetails.getPhones());
            userresponse.put("roles", roles);
            userresponse.put("token", jwtToken);
            userresponse.put("shift", shifts);

            return ResponseHandler.successResponseBuilder(
                    "Login users successfully!.",
                    HttpStatus.OK,
                    userresponse
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        List<DivisionEntity> divisions = (List<DivisionEntity>) userDetails.getDivision();
        UUID id = null;
        List<ShiftEntity> shifts = new ArrayList<>();
        if (!divisions.isEmpty()) {
            id = divisions.get(0).getId();
            shifts = shiftRepository.findByDivisions_Id(id);
        }

        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        Map<String, Object> userResponse = new TreeMap<>();
        userResponse.put("id", userDetails.getId());
        userResponse.put("division", userDetails.getDivision());
        userResponse.put("email", userDetails.getEmail());
        userResponse.put("fullname", userDetails.getFullname());
        userResponse.put("address", userDetails.getAddress());
        userResponse.put("nik", userDetails.getNik());
        userResponse.put("phones", userDetails.getPhones());
        userResponse.put("roles", roles);
        userResponse.put("token", jwtToken);
        userResponse.put("shifts", shifts);

        if (id != null) {
            userResponse.put("firstDivisionId", id);
        }

        return ResponseHandler.successResponseBuilder(
            "Login users successfully!.",
            HttpStatus.OK,
            userResponse
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest data) {
        if (usersRepository.existsByEmail(data.getEmail())) {
            return ResponseHandler.errorResponseBuilder(
                "Email already exists!",
                HttpStatus.BAD_REQUEST
            );
        }

        if (usersRepository.existsByNik(data.getNik())) {
            return ResponseHandler.errorResponseBuilder(
                "NIK ini sudah digunakan!",
                HttpStatus.BAD_REQUEST
            );
        }

        UsersEntity user = new UsersEntity(
            null,
            data.getFullname(),
            data.getNik(),
            data.getAddress(),
            data.getPhones(),
            data.getEmail(),
            encoder.encode(data.getPassword()),
            true,
            null,
            null,
            null,
            null
        );

        Set<String> strDivision = data.getDivision();
        Set<DivisionEntity> divisions = new HashSet<>();
        if (strDivision == null) {
            DivisionEntity userDivision = divisionRepository.findByName(EDivision.UNIT)
                    .orElseThrow(() -> new RuntimeException("Error: Division UNIT is not found."));
            if (!userDivision.getIs_actived()) {
                return ResponseHandler.errorResponseBuilder(
                        "Division UNIT is not active!",
                        HttpStatus.BAD_REQUEST
                );
            }
            divisions.add(userDivision);
        } else {
            for (String divisionName : strDivision) {
                EDivision divisionEnum;
                try {
                    divisionEnum = EDivision.valueOf(divisionName);
                } catch (IllegalArgumentException e) {
                    return ResponseHandler.errorResponseBuilder(
                            "Invalid division name: " + divisionName,
                            HttpStatus.BAD_REQUEST
                    );
                }

                DivisionEntity division = divisionRepository.findByName(divisionEnum)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Division " + divisionEnum + " is not found. [Root cause: Division not found in repository]"));
                if (!division.getIs_actived()) {
                    return ResponseHandler.errorResponseBuilder(
                            "Division " + divisionEnum + " is not active!",
                            HttpStatus.BAD_REQUEST
                    );
                }
                divisions.add(division);
            }
        }

        Set<String> strRoles = data.getRole();
        Set<RolesEntity> roles = new HashSet<>();
        if (strRoles == null) {
            RolesEntity userRole = rolesRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found. [Root cause: Role USER not found in repository]"));
            roles.add(userRole);
        } else {
            for (String role : strRoles) {
                ERole roleEnum;
                try {
                    roleEnum = ERole.valueOf(role.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseHandler.errorResponseBuilder(
                            "Invalid role name: " + role,
                            HttpStatus.BAD_REQUEST
                    );
                }

                RolesEntity roleEntity = rolesRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Error: Role " + roleEnum + " is not found. [Root cause: Role not found in repository]"));

                if (!roleEntity.getIs_actived()) {
                    return ResponseHandler.errorResponseBuilder(
                            "Role " + roleEntity.getName() + " is not active!",
                            HttpStatus.BAD_REQUEST
                    );
                }

                roles.add(roleEntity);
            }
        }

        user.setRoles(roles);
        user.setDivision(divisions);
        UsersEntity savedUser = usersRepository.save(user);

        Map<String, Object> saveList = new HashMap<>();
        saveList.put("id", savedUser.getId());
        saveList.put("fullname", savedUser.getFullname());
        saveList.put("phones", savedUser.getPhones());
        saveList.put("email", savedUser.getEmail());
        saveList.put("roles", savedUser.getRoles());
        saveList.put("divisions", savedUser.getDivision());
        saveList.put("created_at", savedUser.getCreatedAt());
        saveList.put("updated_at", savedUser.getUpdatedAt());

        return ResponseHandler.successResponseBuilder(
            "Save users successfully!",
            HttpStatus.OK,
            saveList
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<DivisionEntity> divisions = (List<DivisionEntity>) userDetails.getDivision();
            UUID id = null;
            List<ShiftEntity> shifts = new ArrayList<>();
            if (!divisions.isEmpty()) {
                id = divisions.get(0).getId();
                shifts = shiftRepository.findByDivisions_Id(id);
            }

            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", userDetails.getId());
            userProfile.put("shift", shifts);
            userProfile.put("email", userDetails.getEmail());
            userProfile.put("fullname", userDetails.getFullname());
            userProfile.put("address", userDetails.getAddress());
            userProfile.put("nik", userDetails.getNik());
            userProfile.put("phones", userDetails.getPhones());
            userProfile.put("roles", userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList()));

            return ResponseHandler.successResponseBuilder(
                    "User profile retrieved successfully!",
                    HttpStatus.OK,
                    userProfile
            );
        } catch (DataAccessException e) {
            throw new NotFoundException("Not found data!." + e.getMessage());
        }
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseHandler.successResponseBuilder(
            "You've been signed out!",
            HttpStatus.OK,
            cookie.toString()
        );
    }
}
