package com._an_5.UNUS.Lobbies;


import com._an_5.UNUS.Users.User;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="lobby")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User host;

    @OneToMany(cascade=CascadeType.ALL)
    private List<User> players;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
