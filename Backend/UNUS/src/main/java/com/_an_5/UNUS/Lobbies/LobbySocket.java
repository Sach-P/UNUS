package com._an_5.UNUS.Lobbies;


import com._an_5.UNUS.Messages.Message;
import com._an_5.UNUS.Messages.MessageRepository;
import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@Controller
@ServerEndpoint(value = "/lobbies/{lobbyId}/{userId}")
public class LobbySocket {

    private static LobbyRepository lobbyRepository;

    private static UserRepository userRepository;

    private static MessageRepository messageRepository;

    @Autowired
    public void setLobbyRepository(LobbyRepository repo){
        lobbyRepository = repo;
    }

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
    private static Map<Session, Lobby> sessionLobbyMap = new Hashtable<>();
    private static Map<Lobby, List<Session>> lobbySessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);


    @OnOpen
    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("userId") int userId) throws IOException {

        logger.info("Entered into Open");

        Lobby lobby = lobbyRepository.findById(lobbyId);

        User player = userRepository.findById(userId);
        sessionUserMap.put(session, player);
        userSessionMap.put(player, session);

        if (!lobbySessionMap.containsKey(lobby)) {
            lobbySessionMap.put(lobby, new ArrayList<>());
        }

        lobbySessionMap.get(lobby).add(session);
        sessionLobbyMap.put(session, lobby);


        sendMessageToParticularUser(player, getChatHistory(lobby));
        JSONObject j = new JSONObject();
        j.put("joined", player.getId());
        broadcast(j.toString(), lobby);
//        String message = player.getUsername() + " has joined the lobby";
//        broadcast(message, lobby);

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        logger.info("Entered into Message: Got Message:" + message);
        if(sessionUserMap.containsKey(session)){
            User player = sessionUserMap.get(session);
            Lobby lobby = sessionLobbyMap.get(session);
            if(message.startsWith("/kick") && player.equals(lobby.getHost())) {
                int userId = Integer.parseInt(message.split(" ")[1]);
                User user = userRepository.findById(userId);
                broadcast(user.getUsername() + " was kicked from the lobby", lobby);
                onClose(userSessionMap.get(user));
            }
            else{
                broadcast(message, lobby);
                messageRepository.save(new Message(player.getUsername(), message, lobby));
            }
        }

    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        User player = sessionUserMap.get(session);
        Lobby lobby = sessionLobbyMap.get(session);
        lobbySessionMap.get(lobby).remove(session);
        sessionLobbyMap.remove(session);
        sessionUserMap.remove(session);
        userSessionMap.remove(player);

        if(lobby.getHost().equals(player)){
            if(lobbySessionMap.get(lobby).size() > 0){
                lobby.setHost(sessionUserMap.get(lobbySessionMap.get(lobby).get(0)));
                lobbyRepository.save(lobby);
            }

        }
        if(lobbySessionMap.get(lobby).size() <= 0){
            lobby.setHost(null);
            lobby.setMessages(null);
            lobbyRepository.save(lobby);
            messageRepository.deleteAllByLobby(lobby);
            lobbyRepository.deleteById(lobby.getId());
        }
        else{
            JSONObject j = new JSONObject();
            j.put("left", player.getId());
            broadcast(j.toString(), lobby);
//            String message = player.getUsername() + " left the lobby";
//            broadcast(message, lobby);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


    private void sendMessageToParticularUser(User user, String message) {
        try {
            userSessionMap.get(user).getBasicRemote().sendText(message);
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }



    private void broadcast(String message, Lobby lobby){
        for(Session session : lobbySessionMap.get(lobby)){
            try{
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Gets the Chat history from the repository
    private String getChatHistory(Lobby lobby) {
        List<Message> messages = messageRepository.findAllByLobby(lobby);

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0) {
            for (Message message : messages) {
                sb.append(message.getUsername() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }



}
