package com._an_5.UNUS;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;

    @OneToMany
    private List<User> friends;


    public User (String username, String password){
        this.username = username;
        this.password = password;
        friends = new ArrayList<>();
    }

    public User() {
        friends = new ArrayList<>();
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

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }



    public List<User> getFriends(){
        return friends;
    }

    public void addFriend(User friend){
        friends.add(friend);
    }

}
