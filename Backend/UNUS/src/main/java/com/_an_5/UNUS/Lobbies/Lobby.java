package com._an_5.UNUS.Lobbies;


import com._an_5.UNUS.Users.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name="lobby")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isPrivate;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private User host;

//    @OneToMany(mappedBy = "lobby")
    @Transient
    private Set<User> players = new HashSet<>();


    public Lobby(User host, boolean isPrivate){
        this.host = host;
        this.isPrivate = isPrivate;
    }

    public Lobby() {
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
}
