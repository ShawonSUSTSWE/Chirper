package com.example.chirper.Models;

public class Users {

    String profile_picture, username, email, password, lastmsg, userId, phoneNo;
    boolean verified_email;

    public Users(String profile_picture, String username, String email, String password, String lastmsg, String userId) {

        this.profile_picture = profile_picture;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastmsg = lastmsg;
        this.userId = userId;

    }
    public Users(String profile_picture, String username, String phoneNo, String password, String lastmsg, String userId, int num) {

        this.profile_picture = profile_picture;
        this.username = username;
        this.phoneNo = phoneNo;
        this.password = password;
        this.lastmsg = lastmsg;
        this.userId = userId;

    }

    public Users (boolean verified_email) {

        this.verified_email = verified_email;

    }


    public  Users() {}

    // This is used for signup with email
    public Users(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;

    }

    //Signup with mobile
    public Users ( String username, String phoneNo, String password, int number ) {
        this.username = username;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public boolean isVerified_email() {
        return verified_email;
    }

    public void setVerified_email(boolean verified_email) {
        this.verified_email = verified_email;
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
    public String getPhoneNo (String phoneNo) {
        return phoneNo;
    }
    public void setPhoneNo (String phoneNo) {
        this.phoneNo = phoneNo;
    }

}
