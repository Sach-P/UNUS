package com._an_5.UNUS;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public User (Long id, String name){
        this.id = id;
        this.name = name;
    }

    public User() {

    }


    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
