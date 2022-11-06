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

    @GetMapping(path = "/test")
    public String test(){
      return "it worked";
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


    @GetMapping(path = "/user/get-lobby/{id}")
    public String getLobbyId(@PathVariable int id){
        User currUser = userRepository.findById(id);
        if(currUser != null){
            if(currUser.getLobby() != null){
                return Integer.toString(currUser.getLobby().getId());
            }
        }

        return -1;
    }




}
