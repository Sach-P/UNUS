package com._an_5.UNUS.Users;

import com._an_5.UNUS.Friends.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private String success = "{\"message\":\"passed\"}";
    private String failure = "{\"message\":\"failed\"}";


    @PostMapping(path = "/signup")
    public String addUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    @PostMapping(path = "/login")
    public Map<String, Object> login(@RequestBody User user){//@ModelAttribute("user") User user){
        HashMap<String, Object> map = new HashMap<>();
        User oauthUser = userService.login(user.getUsername(), user.getPassword());
        if(oauthUser != null){
            map.put("verification", "passed");
            map.put("user", oauthUser);
            return map;
        }
        map.put("verification", "failed");
        map.put("user", null);
        return map;
    }

    @GetMapping(path = "/user")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/user/{id}")
    public User getUser(@PathVariable int id){ return userRepository.findById(id); }

    @DeleteMapping(path = "/user/{id}")
    public void deleteUser(@PathVariable int id) { userRepository.deleteById(id); }

    @PutMapping(path = "/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User currUser = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(user);
        return userRepository.findById(id);
    }

    @PutMapping(path = "/user/{id}/send-friend-request")
    public String sendFriendRequest(@PathVariable int id, @RequestBody Friend friend){
        userService.sendFriendRequest(id, friend);
        return success;
    }

    @GetMapping(path = "/user/{id}/pending-friend-requests")
    public List<Friend> getFriendRequests(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getReceivedFriendRequests();
    }

    @PutMapping(path = "/user/{id}/pending-friend-requests")
    public String acceptOrDeclineFriendRequest(@PathVariable int id, @RequestBody Friend request){
        userService.acceptOrDeclineFriendRequest(id, request);
        return success;
    }

    @PutMapping(path = "/user/{id}/friends/remove-friend")
    public String removeFriend(@PathVariable int id, @RequestBody Friend friend){
        userService.removeFriend(id, friend);
        return success;
    }

    @GetMapping(path = "/user/{id}/friends")
    public List<Friend> getFriends(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getFriends();
    }


}