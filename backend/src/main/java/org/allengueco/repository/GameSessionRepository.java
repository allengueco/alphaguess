package org.allengueco.repository;

import org.allengueco.game.GameSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, UUID> {

}
