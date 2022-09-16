package com._an_5.UNUS;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserInterface extends JpaRepository<User, Long> {

    User findById(int id);

    @Transactional
    void deleteById(int id);
}
