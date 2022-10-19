package com._an_5.UNUS.Lobbies;


import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/lobbies/{lobbyId}")
public class LobbySocket {

    private static LobbyRepository lobbyRepository;

    private static UserRepository userRepository;

    @Autowired
    public void setLobbyRepository(LobbyRepository repo){
        lobbyRepository = repo;
    }

    @Autowired
    public static void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }

    private static Map<Session, User> sessionUserMap = new Hashtable<>();
    private static Map<User, Session> userSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);


    @OnOpen
    public void onOpen(Session session, @RequestParam("userId") int userId) throws IOException {

        logger.info("Entered into Open");

        User player = userRepository.findById(userId);
        sessionUserMap.put(session, player);
        userSessionMap.put(player, session);

        String message = player.getUsername() + " has joined the lobby";
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        User player = sessionUserMap.get(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(player);

        String message = player.getUsername() + " left the lobby";
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }

    private void broadcast(String message){
        sessionUserMap.forEach((session, player) -> {
            try{
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("ExceptionL " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }


}
