package com._an_5.UNUS.Lobbies;


import com._an_5.UNUS.Messages.Message;
import com._an_5.UNUS.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="lobby")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isPrivate;
    private int numPlayers;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private User host;

    @OneToMany(mappedBy = "joinedLobby")
    private Set<User> players = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "lobby")
    private Set<Message> messages = new HashSet<>();


    public Lobby(User host, boolean isPrivate){
        this.host = host;
        this.isPrivate = isPrivate;
        this.numPlayers = 0;
    }

    public Lobby() {
        this.numPlayers = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return id == lobby.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getHost() {
        return host;
    }

    public boolean getPrivacy(){
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Set<User> getPlayers() {
        return players;
    }

    public void setPlayers(Set<User> players){
        this.players = players;
    }


    public void addPlayer(User player){
        players.add(player);
    }

    public void removePlayer(User player) {
        players.remove(player);
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
}
