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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shift")
public class ShiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private Boolean is_actived;

    @Column(nullable = true)
    private String time_start;

    @Column(nullable = true)
    private String time_end;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id")
    // @JsonIgnore
    private DivisionEntity divisions = null;

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public ShiftEntity() {
    }

    public ShiftEntity(UUID id, String name, Boolean is_actived, String time_start, String time_end,
            DivisionEntity divisions, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.is_actived = is_actived;
        this.time_start = time_start;
        this.time_end = time_end;
        this.divisions = divisions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public DivisionEntity getDivisions() {
        return divisions;
    }

    public void setDivisions(DivisionEntity divisions) {
        this.divisions = divisions;
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
