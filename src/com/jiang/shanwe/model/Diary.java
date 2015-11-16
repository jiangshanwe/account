package com.jiang.shanwe.model;

import java.util.Date;

/**
 *
 * @author Jiang.Shanwe
 * 2015-11-5 下午11:10:04
 *
 */
public class Diary {

    private int id;
    private int ownerId;
    private String content;
    private Date diaryDate;
    private Date createdTime;
    private Date updtedTime;
    private int status;
    private int syncStatus;

    public Diary() {
        super();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(Date diaryDate) {
        this.diaryDate = diaryDate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdtedTime() {
        return updtedTime;
    }

    public void setUpdtedTime(Date updtedTime) {
        this.updtedTime = updtedTime;
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
