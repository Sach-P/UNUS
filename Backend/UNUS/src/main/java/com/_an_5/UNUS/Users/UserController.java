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
    UserRepository userRepository;

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
        User currUser = userRepository.findById(id);
        User friendUser = userRepository.findById(friend.getFriendId());
        if(friend != null){
            currUser.addSentFriendRequests(friend);
            friendUser.addReceivedFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
        }
        userRepository.save(currUser);
        userRepository.save(friendUser);
        return success;
    }

    @GetMapping(path = "/user/{id}/pending-friend-requests")
    public List<Friend> getFriendRequests(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getReceivedFriendRequests();
    }

    @PutMapping(path = "/user/{id}/pending-friend-requests")
    public List<Friend> acceptOrDeclineFriendRequest(@PathVariable int id, @RequestBody Friend request){
        User currUser = userRepository.findById(id);
        User friendUser = userRepository.findById(request.getFriendId());
        Iterator<Friend> it = currUser.getReceivedFriendRequests().listIterator();
        while(it.hasNext()){
            Friend friendRequest = it.next();
            if(friendRequest.getFriendId() == request.getFriendId()){
                if(request.getStatus().equals("accepted")){
                    it.remove();
                    friendUser.removeSentFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
                    friendRequest.setStatus("friend");
                    currUser.addFriend(friendRequest);
                    friendUser.addFriend(new Friend(currUser.getId(), currUser.getUsername(), "friend"));
                }
                else if(request.getStatus().equals("declined")){
                    it.remove();
                    friendUser.removeSentFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
                }
                break;
            }
        }
        userRepository.save(currUser);
        userRepository.save(friendUser);
        return currUser.getReceivedFriendRequests();
    }

    @PutMapping(path = "/user/{id}/friends/remove-friend")
    public String removeFriend(@PathVariable int id, @RequestBody Friend friend){
        User currUser = userRepository.findById(id);
        User friendUser = userRepository.findById(friend.getFriendId());
        Iterator<Friend> it = currUser.getFriends().listIterator();
        while(it.hasNext()){
            Friend currFriend = it.next();
            if(friend.getFriendId() == currFriend.getFriendId()){
                it.remove();
                friendUser.removeFriend(new Friend(currUser.getId(), currUser.getUsername()));
            }
        }
        userRepository.save(currUser);
        userRepository.save(friendUser);
        return success;
    }

    @GetMapping(path = "/user/{id}/friends")
    public List<Friend> getFriends(@PathVariable int id){
        User currUser = userRepository.findById(id);
        return currUser.getFriends();
    }


}