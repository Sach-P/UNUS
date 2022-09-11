package com._an_5.project;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("userInterface")
public class UserAccessService implements UserInterface {
    private static List<User> users = new ArrayList<>();

    @Override
    public int insertUser(UUID id, User user){
        users.add(new User(id, user.getName(), user.getAge()));
        return 1;
    }

    @Override
    public List<User> selectAllUsers() {
        return users;
    }

    @Override
    public User selectUser(UUID id) {
        for (User user : users){
            if(user.getId().equals(id)){
                return user;
            }
        }

        return null;
    }


    @Override
    public int deleteUser(UUID id) {
        User user = this.selectUser(id);
        users.remove(user);
        return 1;
    }

    @Override
    public int updateUser(UUID id, User user){
        users.set(users.indexOf(this.selectUser(id)), user);
        return 1;
    }

    @Override
    public List<User> getUsersByAge(int age) {
        List<User> usersByAge = new ArrayList<>();
        for (User user: users){
            if(user.getAge() == age){
                usersByAge.add(user);
            }
        }
        return usersByAge;
    }
}
