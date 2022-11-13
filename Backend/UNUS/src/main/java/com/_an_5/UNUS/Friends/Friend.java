package com._an_5.UNUS.Friends;

import com._an_5.UNUS.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "friends")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "ID of friend",name="fId",required=true)
    @NotNull
    private int fId;

    @ApiModelProperty(notes = "username of friend",name="username",required=true)
    @NotNull
    private String username;

    @ApiModelProperty(notes = "friendship status",name="status",required=true)
    @NotNull
    private String status;

    @ApiModelProperty(notes = "user that is the friend",name="friend",required=false)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private User friend;

    @ApiModelProperty(notes = "user who requested friend request",name="userRequest",required=false)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userRequest_id", referencedColumnName = "id")
    private User userRequest;

    @ApiModelProperty(notes = "user who received the friend request",name="requestedUser",required=false)
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requestedUser_id", referencedColumnName = "id")
    private User requestedUser;


    public Friend(int id, String status, String username){
        this.fId = id;
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
        return id == friend.getId();
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

    public int getFriendId() {
        return fId;
    }

    public void setFriendId(int friendId) {
        this.fId = friendId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(User userRequest) {
        this.userRequest = userRequest;
    }

    public User getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(User requestedUser) {
        this.requestedUser = requestedUser;
    }

    public void setFriend(User friend){
        this.friend = friend;
    }


    public User getFriend() {
        return friend;
    }
}
