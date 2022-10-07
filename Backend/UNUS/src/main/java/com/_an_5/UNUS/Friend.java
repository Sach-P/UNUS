package com._an_5.UNUS;

import javax.persistence.*;

@Entity
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String username;

    public Friend(int id, String username){
        this.id = id;
        this.username = username;
    }

    public Friend (){

    }
}
