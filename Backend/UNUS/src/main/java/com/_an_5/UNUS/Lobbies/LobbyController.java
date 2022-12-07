package com._an_5.UNUS.Lobbies;

import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lobbies")
public class LobbyController {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private LobbyService lobbyService;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failed\"}";

    @ApiOperation(value = "get all current lobbies", response = List.class, tags = "lobby-controller")
    @GetMapping
    public List<Lobby> getLobbies(){
        return lobbyRepository.findAll();
    }

    @ApiOperation(value = "create a lobby and store in the users and lobbies table with one to one relation", response = String.class, tags = "lobby-controller")
    @PostMapping(path = "/create-lobby")
    public String createLobby(@RequestParam(name = "userId") int userId, @RequestBody String json){
        JSONParser jp = new JSONParser(json);
        try{
            boolean isPrivate = Boolean.parseBoolean((String)jp.parseObject().get("private"));
            return lobbyService.createLobby(userId, isPrivate);
        }
        catch (ParseException e){
            e.printStackTrace();
            return failure;
        }
    }

    @ApiOperation(value = "delete a lobby from the lobbies table", response = String.class, tags = "lobby-controller")
    @DeleteMapping(path = "/delete-lobby/{lobbyId}")
    public String deleteLobby(@RequestParam(name = "userId") int userId, @PathVariable int lobbyId){
        return lobbyService.deleteLobby(lobbyId, userId);
    }

    @ApiOperation(value = "Get the number of players in the lobby", response = int.class, tags = "lobby-controller")
    @GetMapping("/player-count/{lobbyId}")
    public int getNumPlayers(@PathVariable int lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId);
        if(lobby != null){
            return lobby.getNumPlayers();
        }

        return -1;
    }

    @ApiOperation(value = "check if a lobby exists in the database", response = String.class, tags = "lobby-controller")
    @GetMapping("/lobby-exists/{lobbyId}")
    public boolean doesLobbyExist(@PathVariable int lobbyId){
        Lobby lobby = lobbyRepository.findById(lobbyId);
        return lobby != null;
    }

}
