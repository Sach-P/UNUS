package com._an_5.UNUS.Users;


import com._an_5.UNUS.Friends.Friend;
import com._an_5.UNUS.Lobbies.Lobby;
import com._an_5.UNUS.Teams.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "user's username",name="username",required=true)
    @NotNull
    private String username;

    @ApiModelProperty(notes = "user's password",name="password",required=true)
    @NotNull
    private String password;

    @ApiModelProperty(notes = "user's role in app (player, admin, guest)", name = "role", required = true)
    @NotNull
    private String role;

//    @ApiModelProperty(notes = "checks if user is online")
//    @NotNull
//    private boolean online;

    @ApiModelProperty(notes = "number of games this user played",name="gamesPlayed",required=false)
    private int gamesPlayed;

    @ApiModelProperty(notes = "number of games this user won",name="gamesWon",required=false)
    private int gamesWon;


    @ApiModelProperty(notes = "Lobby this user is a host of",name="hostedLobby",required=false)
    @JsonIgnore
    @OneToOne(mappedBy = "host")
    private Lobby lobby;

    @ApiModelProperty(notes = "Team this user is a owner of",name="ownedTeam",required=false)
    @JsonIgnore
    @OneToOne(mappedBy = "leader")
    private Team ownedTeam;

//    @ApiModelProperty(notes = "Lobby thi",name="fId",required=true)
//    @JsonIgnore
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private Lobby joinedLobby;

    @ApiModelProperty(notes = "list of friends",name="friends",required=false)
    @OneToMany(mappedBy = "friend")
    private Set<Friend> friends = new HashSet<>();

    @ApiModelProperty(notes = "list of sent friend requests",name="userRequests",required=false)
    @OneToMany(mappedBy = "userRequest")
    private Set<Friend> userRequests = new HashSet<>();

    @ApiModelProperty(notes = "list of received friend requests",name="requestedUsers",required=false)
    @OneToMany(mappedBy = "requestedUser")
    private Set<Friend> requestedUsers = new HashSet<>();

    @ApiModelProperty(notes = "teams this user is in",name="teams",required=false)
    @JsonIgnore
    @ManyToMany(mappedBy = "teamPlayers")
    private Set<Team> teams = new HashSet<>();

    public User (String username, String password){
        this.username = username;
        this.password = password;
    }

    public User() {
        this.role = "player";
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
        this.lobby= lobby;
    }


    public Lobby getLobby(){
        return lobby;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Team getOwnedTeam() {
        return ownedTeam;
    }

    public void setOwnedTeam(Team ownedTeam) {
        this.ownedTeam = ownedTeam;
    }

    //    public Lobby getJoinedLobby() {
//        return joinedLobby;
//    }
//
//    public void setJoinedLobby(Lobby joinedLobby) {
//        this.joinedLobby = joinedLobby;
//    }
}
