package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.SubmitError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Comparator;

public class SubmitGuessState implements State {
    private final Logger LOG = LoggerFactory.getLogger(SubmitGuessState.class);

    @Override
    public ActionResult doAction(GameSession session) {
        ActionResult res = ActionResult.defaultResult(session);
        Instant now = Instant.now();
        String answer = session.getAnswer();
        String guess = session.getGuess();

        if (guess == null || guess.isEmpty() || guess.isBlank()) {
            LOG.info("[guess] is empty. Returning current session state...");
            return res;
        }

        LOG.info("GUESSING: {}", session.getGuess());
        Comparator<String> comparator = session.getGuesses().comparator();

        if (comparator.compare(answer, guess) == 0) {
            res.setGameOver(true);
            res.setLastSubmissionTimestamp(now);
            session.setLastSubmissionTimestamp(now);
            session.setState(new GameCompleteState());
        } else {
            boolean added = session.getGuesses().addGuess(session.getAnswer(), session.getGuess());
            if (!added) {
                LOG.warn("Already guessed: {}", guess);
                res.setError(SubmitError.ALREADY_GUESSED);
            }
            session.setLastSubmissionTimestamp(now);
            res.setLastSubmissionTimestamp(now);
        }
//        res.setGuesses(context.getGuesses());
        LOG.info("{}", session.getGuesses());
        return res;
    }
}
