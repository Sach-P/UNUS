package com._an_5.UNUS;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    //private Map<String, Integer> stats;

//    @OneToMany(mappedBy = "friends")
//    private List<Friend> friends;


    public User (String username, String password){
        this.username = username;
        this.password = password;
//        friends = new ArrayList<>();
//        stats = new HashMap<>();
//        stats.put("Games Played", 0);
//        stats.put("Games Won", 0);
    }

    public User() {
//        friends = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String username){
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() { return password; }

    @JsonSetter
    public void setPassword(String password) { this.password = password; }


//    public List<Friend> getFriends(){
//        return friends;
//    }

//    public void addFriend(Friend friend){
//        friends.add(friend);
//    }

//    public Map<String, Integer> getStats(){
//        return stats;
//    }
//
//    public void updateStat(String key, int val){
//        stats.replace("key", val);
//    }

}
