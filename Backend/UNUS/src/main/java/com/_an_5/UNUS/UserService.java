package com._an_5.UNUS;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserInterface repo;

    public User login(String name, String password){
        User user = repo.findByUsernameAndPassword(name, password);
        return user;
    }
}
