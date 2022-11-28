package com._an_5.UNUS.Teams;

import com._an_5.UNUS.Lobbies.Lobby;
import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

//@Api(value = "team-controller")
@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TeamService teamService;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failed\"}";

    @ApiOperation(value = "get all current teams in the teams table in the database", response = List.class, tags = "team-controller")
    @GetMapping
    public List<Team> getTeams(){
        return teamRepository.findAll();
    }

    @ApiOperation(value = "create a team and store it in the teams and users table with a one to one relation", response = String.class, tags = "team-controller")
    @PostMapping(path = "/create-team")
    public String createTeam(@RequestParam(name = "userId") int userId, @RequestBody Team team){
        User user = userRepository.findById(userId);

        if(user == null || user.getOwnedTeam() != null || user.getRole().equals("guest"))
            return failure;

        team.setLeader(user);

        teamRepository.save(team);
        return success;
    }

    @ApiOperation(value = "join a team (max a player can join is 3)", response = String.class, tags = "team-controller")
    @PutMapping(path = "/join-team/{teamId}")
    public String joinTeam(@RequestParam(name = "userId") int userId, @PathVariable int teamId){
        User user = userRepository.findById(userId);
        if(user == null || user.getTeams().size() >= 3 || user.getRole().equals("guest")){
            return failure;
        }

        Team team = teamRepository.findById(teamId);

        if(team == null || team.getPrivacy()){
            return failure;
        }
        team.addMember(user);
        teamRepository.save(team);
        return success;
    }

    @ApiOperation(value = "leave a team", response = String.class, tags = "team-controller")
    @PutMapping(path = "/leave-team/{teamId}")
    public String leaveTeam(@RequestParam(name = "userId") int userId, @PathVariable int teamId){
        User user = userRepository.findById(userId);
        if(user == null){
            return failure;
        }

        Team team = teamRepository.findById(teamId);

        if(team == null || !team.getPlayers().contains(user)){
            return failure;
        }
        team.removeMember(user);
        teamRepository.save(team);
        return success;
    }


    @ApiOperation(value = "delete a team", response = String.class, tags = "team-controller")
    @DeleteMapping(path = "/delete-team/{teamId}")
    public String deleteTeam(@RequestParam(name = "userId") int userId, @PathVariable int teamId){
        User user = userRepository.findById(userId);
        Team team = teamRepository.findById(teamId);

        if(user == null || team == null)
            return failure;

        if(user.getOwnedTeam().equals(team) || user.getRole().equals("admin")){
            teamRepository.deleteById(teamId);
            return success;
        }
        return failure;
    }




}
