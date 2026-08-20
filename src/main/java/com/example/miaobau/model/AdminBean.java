package com.example.miaobau.model;

public class AdminBean {

    private int adminID;
    private String email;
    private String username;
    private String passwordHash;

    public AdminBean(){}

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getAdminID() {
        return adminID;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
