package com.example.unus;

public class Friend {
    private int userID;
    private String username;
    private int games_played;
    private int games_won;

    public Friend(int userID, String username){
        this.userID = userID;
        this.username = username;
        this.games_played = 0;
        this.games_won = 0;
    }

    public Friend(int userID, String username, int played, int won){
        this.userID = userID;
        this.username = username;
        this.games_played = played;
        this.games_won = won;
    }

    public String getUsername(){
        return username;
    }
    public int getUserID(){
        return userID;
    }
    public int getGamesPlayed(){return games_played;}
    public int getGamesWon(){return games_won;}
}
