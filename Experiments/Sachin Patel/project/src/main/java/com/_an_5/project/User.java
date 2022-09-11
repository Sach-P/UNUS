package com._an_5.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final int age;

    public User (@JsonProperty("id") UUID id,
                 @JsonProperty("name") String name,
                 @JsonProperty("age") int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge(){ return age; }
}
