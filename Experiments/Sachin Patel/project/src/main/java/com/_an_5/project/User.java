package com._an_5.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;

    public User (@JsonProperty("id") UUID id,
                 @JsonProperty("name") String name){
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
