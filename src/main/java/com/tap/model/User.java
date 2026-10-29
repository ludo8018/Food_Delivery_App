package com.tap.model;

import java.sql.Timestamp;

public class User {

    private int userID;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
    private Timestamp createDate;
    private Timestamp lastLoginDate;

    public User() {
    }

    public User(int userID, String name, String username, String password,
                String email, String phoneNumber, String address,
                String role, Timestamp createDate, Timestamp lastLoginDate) {

        this.userID = userID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.createDate = createDate;
        this.lastLoginDate = lastLoginDate;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String toString() {
        return "User [userID=" + userID +
                ", name=" + name +
                ", username=" + username +
                ", email=" + email +
                ", phoneNumber=" + phoneNumber +
                ", address=" + address +
                ", role=" + role +
                ", createDate=" + createDate +
                ", lastLoginDate=" + lastLoginDate + "]";
    }
}
