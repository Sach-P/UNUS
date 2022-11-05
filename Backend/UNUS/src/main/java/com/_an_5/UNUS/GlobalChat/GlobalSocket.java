package com._an_5.UNUS.GlobalChat;


import com._an_5.UNUS.Lobbies.Lobby;
import com._an_5.UNUS.Lobbies.LobbyRepository;
import com._an_5.UNUS.Messages.Message;
import com._an_5.UNUS.Messages.MessageRepository;
import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
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
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/global/{userId}")
public class GlobalSocket {

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

//        sendMessageToParticularUser(player, getChatHistory());
//        String message = player.getUsername() + " has joined the lobby";
//        broadcast(message);

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message: Got Message:" + message);
        if(sessionUserMap.containsKey(session)){
            User user = sessionUserMap.get(session);
//            Lobby lobby = sessionLobbyMap.get(session);
//            if(message.startsWith("/kick") && player.equals(lobby.getHost())) {
//                int userId = Integer.parseInt(message.split(" ")[1]);
//                User user = userRepository.findById(userId);
//                broadcast(user.getUsername() + " was kicked from the lobby");
//                onClose(userSessionMap.get(user));
//            }
//            else{

//            }

            JSONObject j = new JSONObject();
            j.put("message", message);
            j.put("username", user.getUsername());
            broadcast(j.toString());
//            messageRepository.save(new Message(user.getUsername(), message));
        }

    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");
        User user = sessionUserMap.get(session);
//        Lobby lobby = sessionLobbyMap.get(session);
//        if(lobby.getHost().equals(player)){
//            if(lobby.getPlayers().size() > 0){
//                lobby.setHost((User)lobby.getPlayers().toArray()[0]); //TODO test
//                lobby.removePlayer(lobby.getHost());
//                lobbyRepository.save(lobby);
//            }
//            else{
//                lobbyRepository.deleteById(lobby.getId());
//            }
//        }
//        else{
//            lobby.removePlayer(player);
//            lobbyRepository.save(lobby);
//        }
//        lobbySessionMap.remove(lobby);
//        sessionLobbyMap.remove(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(user);
//        String message = "{\"" + user.getUsername() + "\":\"left th\"}";
//        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


//    private void sendMessageToParticularUser(User user, String message) {
//        try {
//            userSessionMap.get(user).getBasicRemote().sendText(message);
//        }
//        catch (IOException e) {
//            logger.info("Exception: " + e.getMessage().toString());
//            e.printStackTrace();
//        }
//    }



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

    // Gets the Chat history from the repository
//    private String getChatHistory() {
//        List<Message> messages = messageRepository.findAll();
//
//        // convert the list to a string
//        StringBuilder sb = new StringBuilder();
//        if(messages != null && messages.size() != 0) {
//            for (Message message : messages) {
//                sb.append(message.getUsername() + ": " + message.getContent() + "\n");
//            }
//        }
//        return sb.toString();
//    }



}
