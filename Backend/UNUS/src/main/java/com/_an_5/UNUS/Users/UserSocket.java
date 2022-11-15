package com._an_5.UNUS.Users;

import com._an_5.UNUS.GlobalChat.GlobalSocket;
import com._an_5.UNUS.Messages.MessageRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class UserSocket {
    private static UserRepository userRepository;

    private static MessageRepository messageRepository;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;
    }

    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        messageRepository = repo;
    }

    private static Map<Session, User> sessionUserMap = new Hashtable<>();
    private static Map<User, Session> userSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(GlobalSocket.class);


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
