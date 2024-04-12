package org.allengueco.service;

import org.allengueco.game.Dictionary;
import org.allengueco.game.GameSession;
import org.allengueco.game.SubmitError;
import org.allengueco.game.WordSelector;
import org.allengueco.game.states.GameStateHandler;
import org.allengueco.repository.GameSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class GameService {
    final GameSessionRepository gameSessionRepository;
    final Dictionary dictionary;
    final WordSelector wordSelector;
    final GameStateHandler stateHandler;
    private final Logger log = LoggerFactory.getLogger(GameService.class);

    public GameService(GameSessionRepository gameSessionRepository, Dictionary dictionary, WordSelector wordSelector, GameStateHandler stateHandler) {
        this.gameSessionRepository = gameSessionRepository;
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
        GameSession session = gameSessionRepository
                .findById(id).orElseGet(() -> newGameSession(id));
        if (guess == null || guess.isEmpty() || guess.isBlank()) {
            log.info("[guess] is empty. Returning current session state if it exists...");
            return Optional.of(stripError(session));
        }

        String normalized = normalized(guess);
        if (dictionary.contains(normalized)) {
            GameSession withGuess = session.mutate()
                    .withGuess(normalized)
                    .build();
            GameSession result = stateHandler.handle(withGuess);
            gameSessionRepository.save(result);
            return Optional.of(result);
        } else {
            return Optional.of(session.mutate()
                    .withError(SubmitError.INVALID_WORD)
                    .build());
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

    GameSession stripError(GameSession session) {
        return session.mutate().withError(null).build();
    }

    String normalized(String s) {
        return s.strip().toLowerCase(Locale.ROOT);
    }
}
