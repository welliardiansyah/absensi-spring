package com.absensi.absensi.dto;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class UserPhotoRequest {
    private UUID id;
    private MultipartFile photo;
    private String type;
    private byte[] bytes;
    private String photoUrl;
    private UUID users;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public MultipartFile getPhoto() {
        return photo;
    }
    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public UUID getUsers() {
        return users;
    }
    public void setUsers(UUID users) {
        this.users = users;
    }
}
