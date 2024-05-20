package com.absensi.absensi.database.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_photo")
public class UserPhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String photo;

    private String type;

    @Lob
    private byte[] bytes;

    private String photoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private UsersEntity users;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UserPhotoEntity() {
    }

    public UserPhotoEntity(UUID id, String photo, String type, byte[] bytes, String photoUrl, UsersEntity users,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.photo = photo;
        this.type = type;
        this.bytes = bytes;
        this.photoUrl = photoUrl;
        this.users = users;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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

    public UsersEntity getUsers() {
        return users;
    }

    public void setUsers(UsersEntity users) {
        this.users = users;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    
}
