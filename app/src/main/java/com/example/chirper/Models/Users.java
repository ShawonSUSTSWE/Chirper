package com.example.chirper.Models;

public class Users {

    String profile_picture, username, email, lastmsg, userId, phoneNo, password, Bio;

    public Users(String profile_picture, String username, String email, String lastmsg, String userId, String Bio) {

        this.profile_picture = profile_picture;
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastmsg = lastmsg;
        this.userId = userId;
        this.Bio = Bio;

    }
    public Users(String profile_picture, String username, String phoneNo, String lastmsg, String userId, String Bio, int num) {

        this.profile_picture = profile_picture;
        this.username = username;
        this.phoneNo = phoneNo;
        this.password = password;
        this.lastmsg = lastmsg;
        this.userId = userId;
        this.Bio = Bio;
    }



    public  Users() {}

    // This is used for signup with email
    public Users(String username, String email, String userId ) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;

    }

    //Signup with mobile
    public Users ( String username, String phoneNo, String userId, int number ) {
        this.username = username;
        this.phoneNo = phoneNo;
        this.password = password;
        this.userId = userId;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
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
    public String getPhoneNo () {

        return phoneNo;
    }
    public void setPhoneNo (String phoneNo) {

        this.phoneNo = phoneNo;
    }

    public String getPassword() {

        return password;

    }

    public void setPassword(String password) {

        this.password = password;

    }
}
