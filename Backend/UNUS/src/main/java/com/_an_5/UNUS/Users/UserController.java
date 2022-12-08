package com._an_5.UNUS.Users;

import com._an_5.UNUS.Teams.Team;
import com._an_5.UNUS.Teams.TeamRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService;

    private String success = "{\"message\":\"passed\"}";
    private String failure = "{\"message\":\"failed\"}";


    @ApiOperation(value = "register a user to the database", response = String.class, tags = "user-controller")
    @PostMapping(path = "/signup")
    public String addUser(@RequestBody User user){
        if (user == null)
            return failure;
        User newUser = new User(user.getUsername(), user.getPassword(), "player");
        userRepository.save(newUser);
        return success;
    }

    @ApiOperation(value = "create a temporary guest user", response = String.class, tags = "user-controller")
    @PostMapping(path = "/create-guest-user")
    public User createGuestUser(){
        User guest = new User();
//        userRepository.save(guest).getId();
        guest.setName("guest" + userRepository.save(guest).getId());
        userRepository.save(guest);
        return guest;
    }

    @ApiOperation(value = "login a user and send user's information when logged in", response = Map.class, tags = "user-controller")
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

    @ApiOperation(value = "Get a list of all users currently in the database users table", response = List.class, tags = "user-controller")
    @GetMapping(path = "/user")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @ApiOperation(value = "get a specific user", response = User.class, tags = "user-controller")
    @GetMapping(path = "/user/{id}")
    public User getUser(@PathVariable int id){ return userRepository.findById(id); }

    @ApiOperation(value = "delete a user", response = String.class, tags = "user-controller")
    @DeleteMapping(path = "/user/{id}")
    public String deleteUser(@PathVariable int id) {
        if(!userRepository.existsById(id))
            return failure;
        User user = userRepository.findById(id);
        user.setLobby(null);
        Set<Team> teams = user.getTeams();
        for (Team team : teams){
            team.removeMember(user);
        }
        teamRepository.saveAll(teams);
        Team ownedTeam = user.getOwnedTeam();
        if(ownedTeam != null) {
            if (ownedTeam.getPlayers().size() > 0) {
                ownedTeam.setLeader((User) ownedTeam.getPlayers().toArray()[0]);
                ownedTeam.removeMember((User) ownedTeam.getPlayers().toArray()[0]);
                teamRepository.save(ownedTeam);
            } else {
                teamRepository.deleteById(ownedTeam.getId());
            }
        }
        userRepository.save(user);
        userRepository.deleteById(id);
        return success;
    }

    @ApiOperation(value = "Update a user in the users table in the database", response = String.class, tags = "user-controller")
    @PutMapping(path = "/user/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        if(user == null)
            return null;
        userRepository.save(user);
        return userRepository.findById(id);
    }

    @ApiOperation(value = "get the lobby ID the user is a host of", response = String.class, tags = "user-controller")
    @GetMapping(path = "/user/get-lobby/{id}")
    public String getLobbyId(@PathVariable int id){
        User currUser = userRepository.findById(id);
        if(currUser != null){
            if(currUser.getLobby() != null){
                return "{\"id\":\""+ currUser.getLobby().getId() + "\"}";
            }
        }
        return failure;
    }


    @ApiOperation(value = "get the team ID the user is a leader of", response = String.class, tags = "user-controller")
    @GetMapping(path = "/user/get-team/{id}")
    public String getTeamId(@PathVariable int id){
        User currUser = userRepository.findById(id);
        if(currUser != null){
            if(currUser.getOwnedTeam() != null){
                return "{\"id\":\""+ currUser.getOwnedTeam().getId() + "\"}";
            }
        }
        return failure;
    }
}