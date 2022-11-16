package com._an_5.UNUS.Teams;

import com._an_5.UNUS.Lobbies.Lobby;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Api(value = "team-controller")
@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

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
    public String createTeam(@RequestParam(name = "userId") int userId, @RequestBody String json){
        JSONParser jp = new JSONParser(json);
        try{
            boolean isPrivate = (boolean)jp.parseObject().get("private");
            return teamService.createTeam(userId, isPrivate);
        }
        catch (ParseException e){
            e.printStackTrace();
            return failure;
        }
    }


}
