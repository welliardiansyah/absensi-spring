package com.absensi.absensi.dto;

import java.util.*;
import com.absensi.absensi.database.entities.EAbsensi;

public class AbsensiRequest {
    private EAbsensi types;
    private String time;
    private String time_start;
    private String time_end;
    private Double latitude;
    private Double longitude;
    private String description;
    private UUID users;

    public EAbsensi getAbsensiType() {
        return types;
    }
    public void setAbsensiType(EAbsensi types) {
        this.types = types;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
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
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public UUID getUsers() {
        return users;
    }
    public void setUsers(UUID users) {
        this.users = users;
    }
}
