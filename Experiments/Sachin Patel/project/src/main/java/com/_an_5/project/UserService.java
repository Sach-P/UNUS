package com._an_5.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserInterface userInterface;

    @Autowired
    public UserService(@Qualifier("userInterface") UserInterface userInterface){
        this.userInterface = userInterface;
    }

    public int addUser(User user){
        return userInterface.insertUser(user);
    }

    public List<User> getAllUsers(){
        return userInterface.selectAllUsers();
    }

    public User getUser(UUID id) { return userInterface.selectUser(id);}

    public int deleteUser(UUID id) { return userInterface.deleteUser(id); }

    public int updateUser(UUID id, User user) { return userInterface.updateUser(id, user); }
}

