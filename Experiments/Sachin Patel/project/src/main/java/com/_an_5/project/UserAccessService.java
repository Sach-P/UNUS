package com._an_5.project;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("userDao")
public class UserAccessService implements UserDao{
    private static List<User> users = new ArrayList<>();

    @Override
    public int insertUser(UUID id, User user){
        users.add(new User(id, user.getName()));
        return 1;
    }

    @Override
    public List<User> selectAllUsers() {
        return users;
    }


    @Override
    public int deleteUser(User user) {
        users.clear();
        return 1;
    }
}
