package com._an_5.UNUS.Lobbies;

import com._an_5.UNUS.Users.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

//@Api(value = "lobby-controller")
@RestController
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
            boolean isPrivate = (boolean)jp.parseObject().get("private");
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

//    @PutMapping("{lobbyId}/join")
//    public String joinLobby(@PathVariable int lobbyId, @RequestParam(name = "userId") int userId){
//        return lobbyService.joinLobby(lobbyId, userId);
//    }

//    @PutMapping("{lobbyId}/kick-player")
//    @SendTo("/lobbies/{lobbyId}/{userId}")
//    public String kickPlayer(@PathVariable int lobbyId, @RequestParam(name = "userId") int userId){
//        return lobbyService.kickPlayer(lobbyId, userId);
//    }
//



//    @GetMapping("/get-players/{lobbyId}")
//    public Set<User> getPlayers(@PathVariable int lobbyId){
//        Lobby lobby = lobbyRepository.findById(lobbyId);
//        if(lobby != null){
//            return lobby.getPlayers();
//        }
//        return null;
//    }

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
        return lobbyRepository.existsById((long)lobbyId);
    }

}
