package com.absensi.absensi.database.entities;

import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "users_profiles", uniqueConstraints = 
    {
        @UniqueConstraint(columnNames = "email"), 
        @UniqueConstraint(columnNames = "phones"), 
        @UniqueConstraint(columnNames = "nik")
    })
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String fullname;
    private String nik;
    private String address;
    private String phones;
    private String email;
    private String password;
    private Boolean is_actived;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_division_id",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "division_id"))
    private Set<DivisionEntity> division = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles_id",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RolesEntity> roles = new HashSet<>();

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UsersEntity() {
    }

    public UsersEntity(String idString) {
        this.id = UUID.fromString(idString);
    }

    public UsersEntity(UUID id, String fullname, String nik, String address, String phones, String email,
            String password, Boolean is_actived, Set<DivisionEntity> division, Set<RolesEntity> roles,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullname = fullname;
        this.nik = nik;
        this.address = address;
        this.phones = phones;
        this.email = email;
        this.password = password;
        this.is_actived = is_actived;
        this.division = division;
        this.roles = roles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIs_actived() {
        return is_actived;
    }

    public void setIs_actived(Boolean is_actived) {
        this.is_actived = is_actived;
    }

    public Set<DivisionEntity> getDivision() {
        return division;
    }

    public void setDivision(Set<DivisionEntity> division) {
        this.division = division;
    }

    public Set<RolesEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesEntity> roles) {
        this.roles = roles;
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
