package com._an_5.UNUS.Users;

import com._an_5.UNUS.GlobalChat.GlobalSocket;
import com._an_5.UNUS.Messages.MessageRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/user/{userId}")
public class UserSocket {
    private static UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }

    private static Map<Session, User> sessionUserMap = new Hashtable<>();
    private static Map<User, Session> userSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(UserSocket.class);


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int userId) throws IOException {

        logger.info("Entered into Open");

        User player = userRepository.findById(userId);
        sessionUserMap.put(session, player);
        userSessionMap.put(player, session);

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message: Got Message:" + message);
        if(sessionUserMap.containsKey(session)){
            User user = sessionUserMap.get(session);

            JSONObject j = new JSONObject();
            j.put("message", message);
            j.put("username", user.getUsername());
            broadcast(j.toString());
        }

    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");
        User user = sessionUserMap.get(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(user);
        if(user.getRole().equals("guest")){
            userRepository.deleteById(user.getId());
        }
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
                logger.info("Exception " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
