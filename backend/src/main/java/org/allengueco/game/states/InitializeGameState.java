package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.Guesses;
import org.allengueco.game.WordSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InitializeGameState implements State {
    private final Logger log = LoggerFactory.getLogger(InitializeGameState.class);

    WordSelector wordSelector;

    public InitializeGameState(WordSelector wordSelector) {
        this.wordSelector = wordSelector;
    }

    @Override
    public ActionResult updateSession(GameSession session) {
        ActionResult res = ActionResult.defaultResult(session);
        String selectedWord = wordSelector.randomWord();
        log.info("selected answer: {}", selectedWord);

        session.setAnswer(selectedWord);
        session.setGuesses(Guesses.empty());
        session.setStart(Instant.now());

        log.info("INITIALIZED");

        session.setState(GameSession.State.Submit);
        return res;
    }
}
