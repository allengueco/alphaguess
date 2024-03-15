package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.Guesses;
import org.allengueco.game.WordSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class InitializeGameState implements State {
    private final Logger LOG = LoggerFactory.getLogger(InitializeGameState.class);

    WordSelector wordSelector;

    public InitializeGameState(WordSelector wordSelector) {
        this.wordSelector = wordSelector;
    }

    @Override
    public ActionResult doAction(GameSession session) {
        ActionResult res = ActionResult.defaultResult(session);
        String selectedWord = wordSelector.randomWord();
        LOG.info("selected answer: {}", selectedWord);

        session.setAnswer(selectedWord);
        session.setGuesses(Guesses.newGuesses());
        session.setStart(Instant.now());

        LOG.info("INITIALIZED");

        session.setState(new SubmitGuessState());
        return res;
    }
}
