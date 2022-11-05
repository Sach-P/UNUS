package com._an_5.UNUS.Messages;

import java.util.Date;

import javax.persistence.*;

import com._an_5.UNUS.Lobbies.Lobby;
import lombok.Data;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Lob
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

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


