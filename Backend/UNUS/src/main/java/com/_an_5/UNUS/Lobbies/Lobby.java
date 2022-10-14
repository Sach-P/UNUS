package com._an_5.UNUS.Lobbies;


import com._an_5.UNUS.Users.User;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="lobby")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isPrivate;

    @OneToOne
    private User host;

    @OneToMany(cascade=CascadeType.ALL)
    private List<User> players;

    public Lobby(User host, boolean isPrivate){
        this.host = host;
        this.isPrivate = isPrivate;
    }

    public Lobby() {
        players = new ArrayList<User>();
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

    public void addPlayer(User player){
        players.add(player);
    }

    public void removePlayer(User player){
        players.remove(player);
    }

    public boolean getPrivacy(){
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


}
