package com.absensi.absensi.database.entities;

import java.time.LocalDateTime;
import java.util.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "users_division")
public class DivisionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EDivision name;

    private Boolean is_actived;

    @Column(nullable = true)
    private String time_start;

    @Column(nullable = true)
    private String time_end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "superior_id")
    @JsonIgnore
    private UsersEntity superior = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private UsersEntity manager = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    @JsonIgnore
    private UsersEntity leader = null;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public DivisionEntity() {
    }

    public DivisionEntity(UUID id, EDivision name, Boolean is_actived, String time_start, String time_end,
            UsersEntity superior, UsersEntity manager, UsersEntity leader, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.is_actived = is_actived;
        this.time_start = time_start;
        this.time_end = time_end;
        this.superior = superior;
        this.manager = manager;
        this.leader = leader;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public EDivision getName() {
        return name;
    }

    public void setName(EDivision name) {
        this.name = name;
    }

    public Boolean getIs_actived() {
        return is_actived;
    }

    public void setIs_actived(Boolean is_actived) {
        this.is_actived = is_actived;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public UsersEntity getSuperior() {
        return superior;
    }

    public void setSuperior(UsersEntity superior) {
        this.superior = superior;
    }

    public UsersEntity getManager() {
        return manager;
    }

    public void setManager(UsersEntity manager) {
        this.manager = manager;
    }

    public UsersEntity getLeader() {
        return leader;
    }

    public void setLeader(UsersEntity leader) {
        this.leader = leader;
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
