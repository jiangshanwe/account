package com.jiang.shanwe.model;

import java.util.Date;

public class City {

    private int id;
    private int ownerId;
    private String cityName;
    private Date createdTime;
    private Date updatedTime;
    private int selectCount;
    private Date lastSelectTime;
    private int status;
    private int syncStatus;

    public City() {
        super();
    }

    public City(int id, String cityName) {
        super();
        this.id = id;
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public Date getLastSelectTime() {
        return lastSelectTime;
    }

    public void setLastSelectTime(Date lastSelectTime) {
        this.lastSelectTime = lastSelectTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

}
