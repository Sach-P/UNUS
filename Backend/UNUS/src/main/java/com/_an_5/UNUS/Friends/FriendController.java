package com._an_5.UNUS.Friends;

import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.JsonParser;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class FriendController {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendService friendService;

    private String success = "{\"message\":\"passed\"}";
    private String failure = "{\"message\":\"failed\"}";

    @PostMapping(path = "/user/{id}/send-friend-request")
    public String sendFriendRequest(@PathVariable int id, @RequestParam("friendId") int friendId){
        return friendService.sendFriendRequest(id, friendId);
    }

    @GetMapping(path = "/user/{id}/pending-friend-requests")
    public Set<Friend> getFriendRequests(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getReceivedFriendRequests();
    }

    @PutMapping(path = "/user/{id}/pending-friend-requests")
    public String acceptOrDeclineFriendRequest(@PathVariable int id, @RequestParam("friendId") int friendId, @RequestBody String json){
        JSONParser jp = new JSONParser(json);
        try{
            String status = (String)jp.parseObject().get("status");
            return friendService.acceptOrDeclineFriendRequest(id, friendId, status);
        }
        catch (ParseException e){
            e.printStackTrace();
            return failure;
        }
    }

    @DeleteMapping(path = "/user/{id}/friends/remove-friend")
    public String removeFriend(@PathVariable int id, @RequestParam("friendId") int friendId){
        friendService.removeFriend(id, friendId);
        return success;
    }

    @GetMapping(path = "/user/{id}/friends")
    public Set<Friend> getFriends(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getFriends();
    }
}
