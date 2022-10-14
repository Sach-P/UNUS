package com._an_5.UNUS.Lobbies;

import com._an_5.UNUS.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    private String success = "{\"message\":\"passed\"}";
    private String failure = "{\"message\":\"failed\"}";

//    @PostMapping(path = "/{userId}/lobby")
//    public String createLobby(@PathVariable int userId, @RequestBody boolean isPrivate){
//
//    }
//
//    @DeleteMapping(path = "")
//    public  String deleteLobby()

}
