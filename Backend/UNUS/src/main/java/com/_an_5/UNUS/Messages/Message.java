package com._an_5.UNUS.Messages;

import java.util.Date;

import javax.persistence.*;

import com._an_5.UNUS.Lobbies.Lobby;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "username of user who sent message",name="username",required=true)
    @NotNull
    @Column
    private String userName;

    @ApiModelProperty(notes = "message the user sent",name="content",required=true)
    @NotNull
    @Lob
    private String content;

    @ApiModelProperty(notes = "date the message was sent",name="sent",required=false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    @ApiModelProperty(notes = "lobby the message was sent in",name="lobby",required=false)
    @ManyToOne
    @JoinColumn(name = "lobby_id", referencedColumnName = "id")
    private Lobby lobby;


    public Message() {};

    public Message(String userName, String content, Lobby lobby) {
        this.userName = userName;
        this.content = content;
        this.lobby = lobby;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}


