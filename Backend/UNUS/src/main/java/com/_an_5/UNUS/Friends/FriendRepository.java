package com._an_5.UNUS.Friends;

import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend findById(int id);

    @Transactional
    void deleteById(int id);
}
