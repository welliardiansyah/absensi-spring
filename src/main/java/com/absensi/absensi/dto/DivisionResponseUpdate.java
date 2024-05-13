package com.absensi.absensi.dto;

import java.sql.Date;

import com.absensi.absensi.database.entities.UsersEntity;

public class DivisionResponseUpdate {
    private Long id;
    private String name;
    private boolean is_actived;
    private Date time_start;
    private Date time_end;
    private UsersEntity superior;
    private UsersEntity manager;
    private UsersEntity leader;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isIs_actived() {
        return is_actived;
    }
    public void setIs_actived(boolean is_actived) {
        this.is_actived = is_actived;
    }
    public Date getTime_start() {
        return time_start;
    }
    public void setTime_start(Date time_start) {
        this.time_start = time_start;
    }
    public Date getTime_end() {
        return time_end;
    }
    public void setTime_end(Date time_end) {
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
}
