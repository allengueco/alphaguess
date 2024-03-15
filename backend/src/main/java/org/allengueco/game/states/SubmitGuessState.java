package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.Guesses;
import org.allengueco.game.SubmitError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Comparator;

@Component
public class SubmitGuessState implements State {
    private final Logger LOG = LoggerFactory.getLogger(SubmitGuessState.class);

    @Override
    public ActionResult updateSession(GameSession session) {
        ActionResult res = ActionResult.defaultResult(session);
        Instant now = Instant.now();
        String answer = session.getAnswer();
        String guess = session.getGuess();
        Guesses guesses = session.getGuesses();

        if (guess == null || guess.isEmpty() || guess.isBlank()) {
            LOG.info("[guess] is empty. Returning current session state...");
            return res;
        }

        LOG.info("GUESSING: {}", session.getGuess());

        switch(guesses.addGuess(answer, guess)) {
            case EQUAL -> {
                res.setGameOver(true);
                res.setLastSubmissionTimestamp(now);
                session.setLastSubmissionTimestamp(now);
                session.setState(GameSession.State.Complete);
            }
            case ALREADY_GUESSED -> {
                LOG.warn("Already guessed: {}", guess);
                res.setError(SubmitError.ALREADY_GUESSED);
            }
            case ADDED -> {
                session.setLastSubmissionTimestamp(now);
                res.setLastSubmissionTimestamp(now);
            }
        }
        LOG.info("{}", guesses);
        return res;
    }
}
