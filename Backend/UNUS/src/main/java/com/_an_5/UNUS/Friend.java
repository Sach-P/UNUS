package com._an_5.UNUS;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Friend {
    @Id
    int friendID;
    String username;

    public Friend(int id, String username){
        friendID = id;
        this.username = username;
    }

    public Friend (){

    }
}
