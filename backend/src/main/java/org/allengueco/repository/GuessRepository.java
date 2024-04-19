package org.allengueco.repository;

import org.allengueco.game.Guess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GuessRepository extends JpaRepository<Guess, UUID> {

}
