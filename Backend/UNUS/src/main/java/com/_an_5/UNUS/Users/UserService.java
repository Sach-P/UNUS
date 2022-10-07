package com._an_5.UNUS.Users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public User login(String username, String password){
        User user = repo.findByUsernameAndPassword(username, password);
        return user;
    }

}
