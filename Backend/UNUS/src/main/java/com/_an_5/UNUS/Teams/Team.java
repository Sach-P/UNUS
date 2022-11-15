package com._an_5.UNUS.Teams;

import com._an_5.UNUS.Users.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ApiModelProperty(notes = "whether the team is private or not",name="isPrivate",required=true)
    private boolean isPrivate;

    @ApiModelProperty(notes = "leader of the team",name="leader",required=true)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private User leader;

    @ApiModelProperty(notes = "users in the team",name="teamPlayers",required=true)
    @ManyToMany
    @JoinTable(
            name = "team_players",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<User> teamPlayers = new HashSet<>();

    @ApiModelProperty(notes = "cumulative wins in the team",name="isPrivate",required=true)
    private int wins;

    public Team(User leader, boolean isPrivate){
        this.leader = leader;
        this.isPrivate = isPrivate;
    }

    public Team(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public User getLeader() {
        return leader;
    }

    public void setPlayers(Set<User> players) {
        this.teamPlayers = players;
    }

    public Set<User> getPlayers() {
        return teamPlayers;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public boolean getPrivacy(){
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}

