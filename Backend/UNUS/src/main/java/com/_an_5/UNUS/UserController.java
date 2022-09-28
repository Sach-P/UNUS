package com._an_5.UNUS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserInterface userInterface;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @PostMapping
    public String addUser(@RequestBody User user){
        if (user == null)
            return failure;
        userInterface.save(user);
        return success;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userInterface.findAll();
    }

    @GetMapping(path = "{id}")
    public User getUser(@PathVariable int id){ return userInterface.findById(id); }

    @DeleteMapping(path = "{id}")
    public void deleteUser(@PathVariable int id) { userInterface.deleteById(id); }

    @PutMapping(path = "{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User currUser = userInterface.findById(id);
        if(currUser == null)
            return null;
        currUser.setName(user.getName());
        userInterface.save(currUser);
        return userInterface.findById(id);
    }

}