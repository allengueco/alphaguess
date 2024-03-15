package org.allengueco.service;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.Dictionary;
import org.allengueco.game.SubmitError;
import org.allengueco.game.WordSelector;
import org.allengueco.game.states.GameSession;
import org.allengueco.game.states.GameStateHandler;
import org.allengueco.game.states.InitializeGameState;
import org.allengueco.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameService {
    private final Logger log = LoggerFactory.getLogger(GameSession.class);
    @Autowired
    GameRepository gameRepository;

    @Autowired
    Dictionary dictionary;

    @Autowired
    WordSelector wordSelector;

    @Autowired
    GameStateHandler stateHandler;

    private static ActionResult decorateWithError(ActionResult r, SubmitError error) {
        r.setError(error);
        return r;
    }

    /**
     * Facade for our game service. Adds guess to an existing session if it exists.
     * If id is new, then we create a new game session and submit the guess.
     *
     * @param id
     * @param guess
     * @return
     */
    public Optional<ActionResult> addGuess(String id, String guess) {
        Optional<GameSession> session = gameRepository
                .findById(id);
        if (guess == null || dictionary.contains(guess)) {
            Optional<GameSession> retrievedOrDefault = session.or(() -> newGameSession(id));
            if (retrievedOrDefault.isPresent()) {
                var s = retrievedOrDefault.get();
                s.setGuess(guess);
                ActionResult res = stateHandler.handle(s);
                gameRepository.save(s);
                return Optional.of(res);
            } else {
                return Optional.empty();
            }
        } else {
            return session.map(s -> stateHandler.handle(s))
                    .map(r -> decorateWithError(r, SubmitError.INVALID_WORD));
        }
    }

    Optional<GameSession> newGameSession(String id) {
        log.info("Creating new session with id {}", id);
        GameSession newSession = GameSession.withId(id);
        newSession.setState(GameSession.State.Initialize);
        stateHandler.handle(newSession); // no need to pass in a guess, this is to initialize session

        return Optional.of(newSession);
    }
}
