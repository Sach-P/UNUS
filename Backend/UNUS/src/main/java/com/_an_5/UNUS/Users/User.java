package com._an_5.UNUS.Users;


import com._an_5.UNUS.Friends.Friend;
import com._an_5.UNUS.Lobbies.Lobby;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.*;

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

    @JsonIgnore
    @OneToOne(mappedBy = "host")
    private Lobby lobby;

    @OneToMany(mappedBy = "friend")
    private Set<Friend> friends = new HashSet<>();

    @OneToMany(mappedBy = "userRequest")
    private Set<Friend> userRequests = new HashSet<>();

    @OneToMany(mappedBy = "requestedUser")
    private Set<Friend> requestedUsers = new HashSet<>();

    public User (String username, String password){
        this.username = username;
        this.password = password;
    }

    public User() {
        gamesPlayed = 0;
        gamesWon = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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


    public Set<Friend> getFriends(){
        return friends;
    }

    public void addFriend(Friend friend){
        friends.add(friend);
    }

    public void removeFriend(Friend friend){
        friends.remove(friend);
    }


    public Set<Friend> getSentFriendRequests(){
        return userRequests;
    }

    public void addSentFriendRequests(Friend friend){
        userRequests.add(friend);
    }

    public void removeSentFriendRequests(Friend friend){
        userRequests.remove(friend);
    }

    public Set<Friend> getReceivedFriendRequests(){
        return requestedUsers;
    }

    public void addReceivedFriendRequests(Friend friend){
        requestedUsers.add(friend);
    }

    public void removeReceivedFriendRequests(Friend friend){
        requestedUsers.remove(friend);
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

    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }

    public Lobby getLobby(){
        return lobby;
    }
}
