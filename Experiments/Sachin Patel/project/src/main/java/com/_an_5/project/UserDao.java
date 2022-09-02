package com._an_5.project;

import java.util.List;
import java.util.UUID;

public interface UserDao {

    int insertUser(UUID ID, User user);

    default int insertUser(User user){
        UUID id = UUID.randomUUID();
        return insertUser(id, user);
    }
    List<User> selectAllUsers();



}
