package com._an_5.UNUS.Users;

import com._an_5.UNUS.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(int id);

    @Transactional
    void deleteById(int id);

//    @Transactional
//    boolean existsById(int id);


    User findByUsernameAndPassword(String name, String password);
}
