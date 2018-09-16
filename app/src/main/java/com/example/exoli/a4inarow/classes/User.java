package com.example.exoli.a4inarow.classes;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String userName;
    private String userID;
    private static int guestID = 1;

    public User() { }

    public User(String email, String userName, String userID) {
        this.email = email;
        this.userName = userName;
        this.userID = userID;
    }

    public static User guest() {
        String guestName = "guest" + guestID++;
        return new User("", guestName, guestName);
    }

    public static User AIUser() {
        return new User("AI", "", "");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}


