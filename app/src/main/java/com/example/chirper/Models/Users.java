package com.example.chirper.Models;

public class Users {

    String profile_picture, username, email, password, lastmsg, userId;

    public Users(String profile_picture, String username, String email, String password, String lastmsg, String userId) {

        this.profile_picture = profile_picture;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastmsg = lastmsg;
        this.userId = userId;

    }

    public  Users() {}

    // This is used for signup
    public Users(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;

    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLastmsg() {
        return lastmsg;
    }

    public void setLastmsg(String lastmsg) {
        this.lastmsg = lastmsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
