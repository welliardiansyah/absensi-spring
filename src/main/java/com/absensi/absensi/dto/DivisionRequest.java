package com.absensi.absensi.dto;

import java.util.*;
import com.absensi.absensi.database.entities.EDivision;

public class DivisionRequest {
    private UUID id;
    private EDivision name;
    private Boolean is_actived;
    private String time_start;
    private String time_end;
    private UUID superior = null;
    private UUID manager = null;
    private UUID leader = null;

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
    public UUID getSuperior() {
        return superior;
    }
    public void setSuperior(UUID superior) {
        this.superior = superior;
    }
    public UUID getManager() {
        return manager;
    }
    public void setManager(UUID manager) {
        this.manager = manager;
    }
    public UUID getLeader() {
        return leader;
    }
    public void setLeader(UUID leader) {
        this.leader = leader;
    }
}
