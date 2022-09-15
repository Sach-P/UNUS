package com._an_5.UNUS;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserInterface extends JpaRepository<User, Long> {

    User findById(int id);
}
