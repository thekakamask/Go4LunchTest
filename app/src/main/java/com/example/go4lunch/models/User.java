package com.example.go4lunch.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class User {

    // UNIQUE ID OF THE USER IN FIREBASE
    private String uid;

    // USERNAME OF THE USER
    private String username;

    // PICTURE OF THE USER
    @Nullable
    private String urlPicture;

    // ID OF A RESTAURANT CHOOSEN BY THE USER
    private String idOfPlace;

    //LIST OF RESTAURANTS LIKED BY THE USER
    private ArrayList<String> like;

    // TIME FOR LUNCH MADE BY THE USER ????
    private int currentTime;

    // IS THE USER IS CONNECTED OF NOT
    private boolean userChat;


    //CONSTRUCTOR
    public User(String uid, String username, @Nullable String urlPicture, String idOfPlace, ArrayList<String> like, int currentTime) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.idOfPlace = idOfPlace;
        this.like = like;
        this.currentTime = currentTime;
        this.userChat = false;
    }

    public User() {

    }

    //GETTERS
    public String getUid() {return uid;}
    public String getUsername() {return username;}
    @Nullable
    public String getUrlPicture() {return urlPicture;}
    public String getIdOfPlace() {return idOfPlace;}
    public ArrayList<String> getLike() { return like; }
    public int getCurrentTime() {return currentTime;}
    public Boolean getUserChat() {return userChat;}

    //SETTERS

    public void setUid(String uid) {
        this.uid=uid;
    }
    public void setUsername(String username) {
        this.username=username;
    }
    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture=urlPicture;
    }
    public void setIdOfPlace(String idOfPlace) {
        this.idOfPlace=idOfPlace;
    }
    public void setLike(ArrayList<String> like) {
        this.like=like;
    }
    public void setCurrentTime(int currentTime) {
        this.currentTime=currentTime;
    }
    public void setUserChat(Boolean userChat) {
        this.userChat=userChat;
    }




}
