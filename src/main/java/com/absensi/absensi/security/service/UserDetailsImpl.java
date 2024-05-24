package com.absensi.absensi.security.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.absensi.absensi.database.entities.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private UUID id;

    private Object division;

    private String username;

    private String fullname;

    private String nik;

    private String address;

    private String phones;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UUID id, Object division, String email, String fullname, String nik, String address, String phones, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.division = division;
        this.username = username;
        this.fullname = fullname;
        this.nik = nik;
        this.address = address;
        this.phones = phones;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public UserDetailsImpl() {
    }

    public static UserDetailsImpl build(UsersEntity user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetails = new UserDetailsImpl(
            user.getId(),
            new ArrayList<>(user.getDivision()), 
            user.getEmail(),
            user.getFullname(),
            user.getNik(),
            user.getAddress(),
            user.getPhones(),
            user.getPassword(),
            authorities);
            
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UUID getId() {
        return id;
    }

    public Object getDivision() {
        return division;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getNik() {
        return nik;
    }

    public String getAddress() {
        return address;
    }

    public String getPhones() {
        return phones;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
