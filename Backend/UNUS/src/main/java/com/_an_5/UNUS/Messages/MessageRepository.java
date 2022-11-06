package com._an_5.UNUS.Messages;

import com._an_5.UNUS.Lobbies.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{

    List<Message> findAllByLobby(Lobby lobby);

    @Transactional
    void deleteAllByLobby(Lobby lobby);

}
