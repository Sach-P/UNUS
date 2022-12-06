package com.example.unus;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private int id;
    private String name;
    private Friend leader;
    private int wins;
    private List<Friend> players;

    public Team(int id, String name,  Friend leader, List<Friend> players) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.players = players;
    }

    public Team(String name, int id, int wins) {
        this.name = name;
        this.id = id;
        this.wins = wins;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Friend getLeader() {
        return leader;
    }

    public List<Friend> getPlayers() {
        return players;
    }

    public int getWins() { return wins; }
}
