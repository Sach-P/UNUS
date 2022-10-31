package com._an_5.UNUS.Lobbies;

import com._an_5.UNUS.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failed\"}";

    @GetMapping(path = "/lobbies")
    public List<Lobby> getLobbies(){
        return lobbyRepository.findAll();
    }

    @PostMapping(path = "/create-lobby")
    public String createLobby(@RequestParam(name = "userId") int userId, @RequestBody boolean isPrivate){
        return lobbyService.createLobby(userId, isPrivate);

    }

    @DeleteMapping(path = "/lobbies/delete-lobby/{lobbyId}")
    public String deleteLobby(@RequestParam(name = "userId") int userId, @PathVariable int lobbyId){
        return lobbyService.deleteLobby(lobbyId, userId);
    }
}
