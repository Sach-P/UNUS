package com._an_5.UNUS;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int friendId;
    String username;
    String status;

    public Friend(int id, String username){
        this.friendId = id;
        this.username = username;
        this.status = "pending";
    }

    public Friend(int id, String username, String status){
        this.friendId = id;
        this.username = username;
        this.status = status;
    }

    public Friend (){
        this.status = "pending";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return friendId == friend.friendId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public int getFriendId(){ return friendId; }

    public void setUsername(String username){
        this.username = username;
    }
}
