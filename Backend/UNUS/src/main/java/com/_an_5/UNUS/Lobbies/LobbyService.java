package com._an_5.UNUS.Lobbies;

import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;

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
        if(host == null || host.getLobby() != null){
            return failure;
        }
        Lobby lobby = new Lobby(host, isPrivate);
        host.setLobby(lobby);
        lobbyRepo.save(lobby);
        userRepo.save(host);
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

//    public String joinLobby(int lobbyId, int userId){
//        Lobby lobby = lobbyRepo.findById(lobbyId);
//        if(lobby == null || lobby.getPrivacy() == true){
//            return failure;
//        }
//        else{
//            User player = userRepo.findById(userId);
//            player.setLobby(lobby);
////            lobby.addPlayer(userRepo.findById(userId));
//            userRepo.save(player);
////            lobbyRepo.save(lobby);
//            return  success;
//        }
//
//    }

//    public String kickPlayer(int lobbyId, int playerId){
//        Lobby lobby = lobbyRepo.findById(lobbyId);
//        if(lobby != null){
//                User player = userRepo.findById(playerId);
//                Iterator<User> it = lobby.getPlayers().iterator();
//                while(it.hasNext()) {
//                    User user = it.next();
//                    if (user.getId() == playerId) {
//                        user.setLobby(null);
//                        userRepo.save(user);
//                        return "/kick " + playerId;
//                    }
//
//                }
//        }
//        return failure;
//    }
}
