package com.example.unus;

public class Friend {
    private int userID;
    private String username;

    public Friend(int userID, String username){
        this.userID = userID;
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
    public int getUserID(){
        return userID;
    }
}
