package org.allengueco.repository;

import org.allengueco.game.states.GameSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<GameSession, String> {
}
