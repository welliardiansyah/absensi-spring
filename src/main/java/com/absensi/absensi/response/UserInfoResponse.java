package com.absensi.absensi.response;

import java.util.*;

public class UserInfoResponse {
    private UUID id;
    private String email;
    private List<String> roles;
    private String token;
    
    public UserInfoResponse(UUID id, String email, List<String> roles, String token) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
