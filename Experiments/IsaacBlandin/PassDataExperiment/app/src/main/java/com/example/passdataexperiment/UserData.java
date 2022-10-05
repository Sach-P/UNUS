package com.example.passdataexperiment;

/**
 * Singleton class used to allow userdata to be stored and read from any activity or fragment
 */
public class UserData {

    private String userID;
    private String username;

    private int gamesPlayed;
    private int gamesWon;

    private UserData singleInstance;

    private UserData(){

    }

    public UserData getInstance(){
        if (singleInstance == null){
            singleInstance = new UserData();
        }

        return singleInstance;
    }

}
