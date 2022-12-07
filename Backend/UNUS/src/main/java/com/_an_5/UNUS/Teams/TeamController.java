package com._an_5.UNUS.Teams;

import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
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
    @GetMapping("/teams")
    public List<Team> getTeams(){
        return teamRepository.findAll();
    }

    @ApiOperation(value = "get a team from the database", response = Team.class, tags = "team-controller")
    @GetMapping("/teams/{teamId}")
    public Team getTeam(@PathVariable int teamId){
        return teamRepository.findById(teamId);
    }

    @ApiOperation(value = "create a team and store it in the teams and users table with a one to one relation", response = String.class, tags = "team-controller")
    @PostMapping(path = "/teams/create-team")
    public String createTeam(@RequestParam(name = "userId") int userId, @RequestBody Team team){
        User user = userRepository.findById(userId);

        if(user == null || user.getOwnedTeam() != null || user.getRole().equals("guest"))
            return failure;

        team.setLeader(user);

        teamRepository.save(team);
        return success;
    }

    @ApiOperation(value = "join a team (max a player can join is 3)", response = String.class, tags = "team-controller")
    @PutMapping(path = "/teams/join-team/{teamId}")
    public String joinTeam(@RequestParam(name = "userId") int userId, @PathVariable int teamId){
        User user = userRepository.findById(userId);
        if(user == null || user.getTeams().size() >= 3 || user.getRole().equals("guest")){
            return failure;
        }

        Team team = teamRepository.findById(teamId);

        if(team == null || team.getPrivacy()){
            return failure;
        }

        team.setWins(team.getWins() + user.getGamesWon());
        team.addMember(user);
        teamRepository.save(team);
        return success;
    }

    @ApiOperation(value = "leave a team", response = String.class, tags = "team-controller")
    @PutMapping(path = "/teams/leave-team/{teamId}")
    public String leaveTeam(@RequestParam(name = "userId") int userId, @PathVariable int teamId){
        User user = userRepository.findById(userId);
        if(user == null){
            return failure;
        }

        Team team = teamRepository.findById(teamId);

        if(team == null || !team.getPlayers().contains(user)){
            return failure;
        }
        team.setWins(team.getWins() - user.getGamesWon());
        team.removeMember(user);
        teamRepository.save(team);
        return success;
    }


    @ApiOperation(value = "delete a team", response = String.class, tags = "team-controller")
    @DeleteMapping(path = "/teams/delete-team/{teamId}")
    public String deleteTeam(@RequestParam(name = "userId") int userId, @PathVariable int teamId){
        User user = userRepository.findById(userId);
        Team team = teamRepository.findById(teamId);

        if(user == null || team == null)
            return failure;

        if(user.getOwnedTeam().equals(team) || user.getRole().equals("admin")){
            team.setLeader(null);
            team.setPlayers(null);
            teamRepository.deleteById(teamId);
            return success;
        }
        return failure;
    }

    @ApiOperation(value = "adjust games played and won stats", response = String.class, tags = "team-controller")
    @PutMapping(path = "/gameEnd/{id}")
    public String incrementGamesStat(@PathVariable int id, @RequestBody String json){
        User user = userRepository.findById(id);
        if(user == null){
            return failure;
        }

        JSONParser jp = new JSONParser(json);
        try{
            boolean win = Boolean.parseBoolean((String)jp.parseObject().get("win"));
            if(win){
                user.setGamesWon(user.getGamesWon() + 1);
                Team ownedTeam = user.getOwnedTeam();
                if(ownedTeam != null){
                    ownedTeam.setWins(ownedTeam.getWins() + 1);
                    teamRepository.save(ownedTeam);
                }
                Set<Team> teams = user.getTeams();
                if(teams != null){
                    for(Team team : teams){
                        team.setWins(team.getWins() + 1);
                    }
                    teamRepository.saveAll(teams);
                }

            }
            user.setGamesPlayed(user.getGamesPlayed() + 1);
            userRepository.save(user);
            return success;
        }
        catch (ParseException e){
            e.printStackTrace();
            return failure;
        }
    }
}
