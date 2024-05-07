package com.absensi.absensi.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.absensi.absensi.database.entities.DivisionEntity;
import com.absensi.absensi.database.entities.EDivision;
import com.absensi.absensi.database.entities.ERole;
import com.absensi.absensi.database.entities.RolesEntity;
import com.absensi.absensi.database.entities.UsersEntity;
import com.absensi.absensi.database.repository.DivisionRepository;
import com.absensi.absensi.database.repository.RolesRepository;
import com.absensi.absensi.database.repository.UsersRepository;
import com.absensi.absensi.dto.LoginRequest;
import com.absensi.absensi.dto.RegisterRequest;
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
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateJwtToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        Map<String, Object> userresponse = new TreeMap<>();
        userresponse.put("id", userDetails.getId());
        userresponse.put("division", userDetails.getDivision());
        userresponse.put("email", userDetails.getEmail());
        userresponse.put("fullname", userDetails.getFullname());
        userresponse.put("address", userDetails.getAddress());
        userresponse.put("nik", userDetails.getNik());
        userresponse.put("phones", userDetails.getPhones());
        userresponse.put("roles", roles);
        userresponse.put("token", jwtToken);

        return ResponseHandler.successResponseBuilder(
            "Login users successfully!.",
            HttpStatus.OK,
            userresponse
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

        Set<String> strDivision= data.getRole();
        Set<DivisionEntity> divisions = new HashSet<>();
        if (strDivision == null) {
            DivisionEntity userRole = divisionRepository.findByName(EDivision.UNIT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    divisions.add(userRole);
        } else {
            strDivision.forEach(role -> {
                switch (role) {
                    case "admin":
                    DivisionEntity adminRole = divisionRepository.findByName(EDivision.DIREKTUR_UTAMA)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        divisions.add(adminRole);
                        break;
                    case "reporting":
                    DivisionEntity reportingRole = divisionRepository.findByName(EDivision.WAKIL_DIREKTUR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                divisions.add(reportingRole);
                        break;
                    case "hr":
                    DivisionEntity hrRole = divisionRepository.findByName(EDivision.ENGINEER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                divisions.add(hrRole);
                        break;
                    case "payroll":
                    DivisionEntity payrollRole = divisionRepository.findByName(EDivision.HEAD)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                divisions.add(payrollRole);
                        break;
                    default:
                    DivisionEntity userRole = divisionRepository.findByName(EDivision.IT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                divisions.add(userRole);
                }
            });
        }

        Set<String> strRoles = data.getRole();
        Set<RolesEntity> roles = new HashSet<>();
        if (strRoles == null) {
            RolesEntity userRole = rolesRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        RolesEntity adminRole = rolesRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "reporting":
                        RolesEntity reportingRole = rolesRepository.findByName(ERole.REPORTING)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(reportingRole);
                        break;
                    case "hr":
                        RolesEntity hrRole = rolesRepository.findByName(ERole.HR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(hrRole);
                        break;
                    case "payroll":
                        RolesEntity payrollRole = rolesRepository.findByName(ERole.PAYROLL)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(payrollRole);
                        break;
                    default:
                        RolesEntity userRole = rolesRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
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
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("id", userDetails.getId());
        userProfile.put("division", userDetails.getDivision());
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
