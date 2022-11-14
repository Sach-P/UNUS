package com._an_5.UNUS.Teams;

import com._an_5.UNUS.Users.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User leader;

    @ManyToMany
    private Set<User> players;

    private int wins;

    public Team(){

    }

}
