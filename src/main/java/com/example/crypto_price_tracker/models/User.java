package com.example.crypto_price_tracker.models;
public class User {
    private static int userCounter=1;
    private String userID;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String passwordKey;
    public User(String userName, String userEmail, String userPassword, String passwordKey) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.passwordKey = passwordKey;
        this.userID = "USER"+userCounter++;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getPasswordKey() {
        return passwordKey;
    }
    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }
}
