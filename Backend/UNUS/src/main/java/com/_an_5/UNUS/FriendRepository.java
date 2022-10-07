package com._an_5.UNUS;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Friend findById(int id);

    @Transactional
    void deleteById(int id);
}
