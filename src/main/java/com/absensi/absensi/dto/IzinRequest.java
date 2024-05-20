package com.absensi.absensi.dto;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import com.absensi.absensi.database.entities.EIzin;

public class IzinRequest {
    private UUID id;
    private EIzin name;
    private String dateStart;
    private String dateEnd;
    private String description;
    private MultipartFile photo;
    private UUID users;
    private Boolean is_actived;
    private String type;
    private byte[] bytes;
    private String photoUrl;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EIzin getName() {
        return name;
    }

    public void setName(EIzin name) {
        this.name = name;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public UUID getUsers() {
        return users;
    }

    public void setUsers(UUID users) {
        this.users = users;
    }

    public Boolean getIs_actived() {
        return is_actived;
    }

    public void setIs_actived(Boolean is_actived) {
        this.is_actived = is_actived;
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
}