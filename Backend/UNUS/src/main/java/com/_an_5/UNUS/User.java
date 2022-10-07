package com._an_5.UNUS;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private int gamesPlayed;
    private int gamesWon;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="friends_id")
    private List<Friend> friends;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="friendRequest_id")
    private List<Friend> friendRequests;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="friendRequested_id")
    private List<Friend> friendRequested;

    public User (String username, String password){
        this.username = username;
        this.password = password;
        gamesPlayed = 0;
        gamesWon = 0;
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
        friendRequested = new ArrayList<>();
    }

    public User() {
        friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
        friendRequested = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String username){
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() { return password; }

    @JsonSetter
    public void setPassword(String password) { this.password = password; }


    public List<Friend> getFriends(){
        return friends;
    }

    public void addFriend(Friend friend){
        friends.add(friend);
    }

    public void removeFriend(Friend friend){
        friends.remove(friend);
    }


    public List<Friend> getSentFriendRequests(){
        return friendRequests;
    }

    public void addSentFriendRequests(Friend friend){
        friendRequests.add(friend);
    }

    public void removeSentFriendRequests(Friend friend){
        friendRequests.remove(friend);
    }

    public List<Friend> getReceivedFriendRequests(){
        return friendRequested;
    }

    public void addReceivedFriendRequests(Friend friend){
        friendRequested.add(friend);
    }

    public void removeReceivedFriendRequests(Friend friend){
        friendRequested.remove(friend);
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesWon() {
        return gamesWon;
    }


}
