package com._an_5.UNUS.Teams;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findById(int id);

    @Transactional
    void deleteById(int id);
}
