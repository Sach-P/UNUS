package com._an_5.UNUS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserInterface userInterface;

    @Autowired
    public UserController(UserInterface userInterface){
        this.userInterface = userInterface;
    }

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
    public User getUser(@PathVariable("id") int id){ return userInterface.findById(id); }

    @DeleteMapping(path = "{id}")
    public void deleteUser(@PathVariable("id") int id) { userInterface.deleteById(id); }

    @PutMapping(path = "{id}")
    public void updateUser(@PathVariable("id") int id, @RequestBody User user) {  }

    @GetMapping(path = "/age/{age}")
    public List<User> getUsersByAge(@PathVariable("age") int age){ return null; }
}