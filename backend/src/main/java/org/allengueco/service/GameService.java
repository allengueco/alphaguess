package org.allengueco.service;

import org.allengueco.game.Dictionary;
import org.allengueco.game.GameSession;
import org.allengueco.game.WordSelector;
import org.allengueco.game.states.GameStateHandler;
import org.allengueco.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {
    final GameRepository gameRepository;
    final Dictionary dictionary;
    final WordSelector wordSelector;
    final GameStateHandler stateHandler;
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    public GameService(GameRepository gameRepository, Dictionary dictionary, WordSelector wordSelector, GameStateHandler stateHandler) {
        this.gameRepository = gameRepository;
        this.dictionary = dictionary;
        this.wordSelector = wordSelector;
        this.stateHandler = stateHandler;
    }

    /**
     * Facade for our game service. Adds guess to an existing session if it exists.
     * If id is new, then we create a new game session and submit the guess.
     * Otherwise, if guess is not a word, then we return an empty optional.
     *
     * @param id    session id to add guess to
     * @param guess user's guess
     * @return modified game session if guess is successful, or empty
     */
    public Optional<GameSession> addGuess(String id, String guess) {
        GameSession session = gameRepository
                .findById(id).orElseGet(() -> newGameSession(id));

        if (guess == null || dictionary.contains(guess)) {
            GameSession withGuess = session.mutate()
                    .withGuess(guess)
                    .build();
            GameSession result = stateHandler.handle(withGuess);
            gameRepository.save(result);
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    GameSession newGameSession(String id) {
        log.info("Creating new session with id {}", id);
        GameSession newSession = new GameSession(id,
                null,
                null, // no guess needed
                GameSession.State.Initialize,
                null,
                null,
                null,
                null,
                false);
        return stateHandler.handle(newSession);
    }
}
