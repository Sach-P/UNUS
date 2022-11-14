package com._an_5.UNUS.Friends;

import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.JsonParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Api(value = "friend-controller")
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

    @ApiOperation(value = "Send a friend request to another user", response = String.class, tags = "friend-controller")
    @PostMapping(path = "/user/{id}/send-friend-request")
    public String sendFriendRequest(@PathVariable int id, @RequestParam("friendId") int friendId){
        return friendService.sendFriendRequest(id, friendId);
    }

    @ApiOperation(value = "Get friend requests received by user", response = Set.class, tags = "friend-controller")
    @GetMapping(path = "/user/{id}/pending-friend-requests")
    public Set<Friend> getFriendRequests(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getReceivedFriendRequests();
    }

    @ApiOperation(value = "accept or decline a friend request from another user", response = String.class, tags = "friend-controller")
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

    @ApiOperation(value = "remove a friend", response = String.class, tags = "friend-controller")
    @DeleteMapping(path = "/user/{id}/friends/remove-friend")
    public String removeFriend(@PathVariable int id, @RequestParam("friendId") int friendId){
        friendService.removeFriend(id, friendId);
        return success;
    }

    @ApiOperation(value = "geta a list of a user's friends", response = String.class, tags = "friend-controller")
    @GetMapping(path = "/user/{id}/friends")
    public Set<Friend> getFriends(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getFriends();
    }
}
