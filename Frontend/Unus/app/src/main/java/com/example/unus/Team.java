package com.example.unus;

public class Team {

    private int id;
    private String name;

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
