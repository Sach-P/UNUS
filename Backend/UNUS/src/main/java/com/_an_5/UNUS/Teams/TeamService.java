package com._an_5.UNUS.Teams;

import com._an_5.UNUS.Lobbies.Lobby;
import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    TeamRepository teamRepo;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failed\"}";

    public String createTeam(int userId, boolean isPrivate){
        User leader = userRepo.findById(userId);
        if(leader == null || leader.getOwnedTeam() != null){
            return failure;
        }
        Team team = new Team(leader, isPrivate);
        leader.setOwnedTeam(team);
        teamRepo.save(team);
        userRepo.save(leader);
        return success;
    }
}
