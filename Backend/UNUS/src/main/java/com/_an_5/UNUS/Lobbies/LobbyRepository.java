package com._an_5.UNUS.Lobbies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Lobby findById(int id);

    @Transactional
    void deleteById(int id);

}
