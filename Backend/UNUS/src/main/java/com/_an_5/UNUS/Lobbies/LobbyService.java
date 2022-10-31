package com._an_5.UNUS.Lobbies;

import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyService {
    @Autowired
    private LobbyRepository lobbyRepo;

    @Autowired
    private UserRepository userRepo;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failed\"}";

    public String createLobby(int userId, boolean isPrivate){
        User host = userRepo.findById(userId);
        if(host == null){
            return failure;
        }
        lobbyRepo.save(new Lobby(host, isPrivate));
        return success;
    }

    public String deleteLobby(int lobbyId, int userId){
        User host = userRepo.findById(userId);
        Lobby lobby = lobbyRepo.findById(lobbyId);
        if(host.equals(lobby.getHost())){
            lobby.setHost(null);
            lobbyRepo.save(lobby);
            lobbyRepo.deleteById(lobbyId);
            return success;
        }
        else{
            return failure;
        }
    }

    public String joinLobby(int lobbyId, int userId){
        Lobby lobby = lobbyRepo.findById(lobbyId);
        if(lobby == null || lobby.getPrivacy() == true){
            return failure;
        }
        else{
            lobby.addPlayer(userRepo.findById(userId));
            lobbyRepo.save(lobby);
            return  success;
        }

    }
}
