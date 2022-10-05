package com._an_5.UNUS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserInterface userInterface;

    @Autowired
    private UserService userService;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @PostMapping(path = "/signup")
    public String addUser(@RequestBody User user){
        if (user == null)
            return failure;
        userInterface.save(user);
        return success;
    }

//    @GetMapping(path = "/login")
//    public ModelAndView login(){
//        ModelAndView mav = new ModelAndView("login");
//        mav.addObject("user", new User());
//        return mav;
//    }

    @PostMapping(path = "/login")
    public String login(@RequestBody User user){//@ModelAttribute("user") User user){
        User oauthUser = userService.login(user.getUsername(), user.getPassword());
        if(oauthUser != null){
            return "{\"verification\":\"passed\"}";
        }
        return "{\"verification\":\"failed\"}";
    }


    @GetMapping(path = "/user")
    public List<User> getAllUsers(){
        return userInterface.findAll();
    }

    @GetMapping(path = "/user/{id}")
    public User getUser(@PathVariable int id){ return userInterface.findById(id); }

    @DeleteMapping(path = "/user/{id}")
    public void deleteUser(@PathVariable int id) { userInterface.deleteById(id); }

    @PutMapping(path = "/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User currUser = userInterface.findById(id);
        if(user == null)
            return null;
        userInterface.save(user);
        return userInterface.findById(id);
    }

}