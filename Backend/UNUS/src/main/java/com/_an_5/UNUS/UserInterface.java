package com._an_5.UNUS;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserInterface extends JpaRepository<User, Long> {

    User findById(int id);

    @Transactional
    void deleteById(int id);

    User findByUsernameAndPassword(String name, String password);
}
