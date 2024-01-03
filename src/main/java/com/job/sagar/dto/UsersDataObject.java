package com.job.sagar.dto;

public class UsersDataObject {
    private Long id;
    private String userName;
    private int userTypeId;
    private String password;
    private char isActive;
    private char smsNotification;
    private char emailNotification;

    public UsersDataObject() {
    }

    public UsersDataObject(Long id, String userName, int userTypeId, String password, char isActive, char smsNotification, char emailNotification) {
        this.id = id;
        this.userName = userName;
        this.userTypeId = userTypeId;
        this.password = password;
        this.isActive = isActive;
        this.smsNotification = smsNotification;
        this.emailNotification = emailNotification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getIsActive() {
        return isActive;
    }

    public void setIsActive(char isActive) {
        this.isActive = isActive;
    }

    public char getSmsNotification() {
        return smsNotification;
    }

    public void setSmsNotification(char smsNotification) {
        this.smsNotification = smsNotification;
    }

    public char getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(char emailNotification) {
        this.emailNotification = emailNotification;
    }
}
