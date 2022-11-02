package com._an_5.UNUS.Lobbies;

import com._an_5.UNUS.Users.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Slf4j
//@AllArgsConstructor
@RequestMapping("/lobbies")
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

//    private final SimpMessagingTemplate simpMessagingTemplate;
//
//    @Autowired
//    public LobbyController(SimpMessagingTemplate template){
//        this.simpMessagingTemplate = template;
//    }


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failed\"}";

    @GetMapping
    public List<Lobby> getLobbies(){
        return lobbyRepository.findAll();
    }

    @PostMapping(path = "/create-lobby")
    public String createLobby(@RequestParam(name = "userId") int userId, @RequestBody boolean isPrivate){
        return lobbyService.createLobby(userId, isPrivate);

    }

    @DeleteMapping(path = "/delete-lobby/{lobbyId}")
    public String deleteLobby(@RequestParam(name = "userId") int userId, @PathVariable int lobbyId){
        return lobbyService.deleteLobby(lobbyId, userId);
    }

    @PutMapping("{lobbyId}/join")
    public String joinLobby(@PathVariable int lobbyId, @RequestParam(name = "userId") int userId){
        return lobbyService.joinLobby(lobbyId, userId);
    }

    @PutMapping("{lobbyId}/kick-player")
    @SendTo("/lobbies/{lobbyId}/{userId}")
    public String kickPlayer(@PathVariable int lobbyId, @RequestParam(name = "userId") int userId){
        return lobbyService.kickPlayer(lobbyId, userId);
    }

}
