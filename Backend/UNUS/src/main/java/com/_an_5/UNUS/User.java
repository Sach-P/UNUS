package com._an_5.UNUS;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public User (int id, String name){
        this.id = id;
        this.name = name;
    }

    public User() {

    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
