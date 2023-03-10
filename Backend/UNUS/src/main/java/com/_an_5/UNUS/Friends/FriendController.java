package com._an_5.UNUS.Friends;

import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
public class FriendController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendService friendService;

    private String success = "{\"message\":\"passed\"}";
    private String failure = "{\"message\":\"failed\"}";

    @ApiOperation(value = "Send a friend request to another user this will also show as a received friend request from the user it was sent to", response = String.class, tags = "friend-controller")
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

    @ApiOperation(value = "remove a friend the will also remove the friend for the users table because of the many to one relation", response = String.class, tags = "friend-controller")
    @DeleteMapping(path = "/user/{id}/friends/remove-friend")
    public String removeFriend(@PathVariable int id, @RequestParam("friendId") int friendId){
        return friendService.removeFriend(id, friendId);
    }

    @ApiOperation(value = "get a list of a user's friends", response = String.class, tags = "friend-controller")
    @GetMapping(path = "/user/{id}/friends")
    public Set<Friend> getFriends(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getFriends();
    }
}
