package com._an_5.UNUS.Users;

import com._an_5.UNUS.Friends.Friend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Api(value = "UserController")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private String success = "{\"message\":\"passed\"}";
    private String failure = "{\"message\":\"failed\"}";


    @ApiOperation(value = "register a user", response = String.class, tags = "addUser")
    @PostMapping(path = "/signup")
    public String addUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    @ApiOperation(value = "login a user", response = Map.class, tags = "login")
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

    @ApiOperation(value = "Get a list of all users", response = List.class, tags = "getAllUsers")
    @GetMapping(path = "/user")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @ApiOperation(value = "get a specific user", response = User.class, tags = "getUser")
    @GetMapping(path = "/user/{id}")
    public User getUser(@PathVariable int id){ return userRepository.findById(id); }

    @ApiOperation(value = "delete a user", response = String.class, tags = "deleteUser")
    @DeleteMapping(path = "/user/{id}")
    public String deleteUser(@PathVariable int id) {
        if(!userRepository.existsById((long)id))
            return failure;
        userRepository.deleteById(id);
        return success;
    }

    @ApiOperation(value = "Update a user", response = String.class, tags = "sendFriendRequest")
    @PutMapping(path = "/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User currUser = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(user);
        return userRepository.findById(id);
    }

    @ApiOperation(value = "get the lobby ID the user is a host of", response = int.class, tags = "getLobbyId")
    @GetMapping(path = "/user/get-lobby/{id}")
    public int getLobbyId(@PathVariable int id){
        User currUser = userRepository.findById(id);
        if(currUser != null){
            if(currUser.getLobby() != null){
                return currUser.getLobby().getId();
            }
        }

        return -1;
    }




}