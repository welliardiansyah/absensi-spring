package com.absensi.absensi.dto;

import java.util.*;

public class ShiftRequest {
    private UUID id;
    private String name;
    private Boolean is_actived;
    private String time_start;
    private String time_end;
    private UUID divisions;

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
    public UUID getDivisions() {
        return divisions;
    }
    public void setDivisions(UUID divisions) {
        this.divisions = divisions;
    }
}
