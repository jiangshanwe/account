package com.jiang.shanwe.model;

import java.util.Date;

/**
 *
 * @author Jiang.Shanwe
 * 2015-11-5 下午11:11:09
 *
 */
public class User {

    private int id;
    private String phoneNum;
    private String name;
    private int sex;
    private String numberPwd;
    private String picturePwd;
    private String email;
    private Date registedTime;
    private Date updatedTime;
    private Date lastLoginTime;
    private Date bithday;
    private String icon;
    private int status;
    private int syncStatus;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public User() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNumberPwd() {
        return numberPwd;
    }

    public void setNumberPwd(String numberPwd) {
        this.numberPwd = numberPwd;
    }

    public String getPicturePwd() {
        return picturePwd;
    }

    public void setPicturePwd(String picturePwd) {
        this.picturePwd = picturePwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistedTime() {
        return registedTime;
    }

    public void setRegistedTime(Date registedTime) {
        this.registedTime = registedTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getBithday() {
        return bithday;
    }

    public void setBithday(Date bithday) {
        this.bithday = bithday;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
