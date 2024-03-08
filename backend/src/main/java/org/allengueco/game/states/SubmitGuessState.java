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
    public ActionResult doAction(GameContext context) {
        ActionResult res = ActionResult.defaultResult(context);
        Instant now = Instant.now();
        String answer = context.getAnswer();
        String guess = context.getGuess();

        if (guess == null || guess.isEmpty() || guess.isBlank()) {
            LOG.info("[guess] is empty. Returning res");
            return res;
        }

        LOG.info("GUESSING: {}", context.getGuess());
        Comparator<String> comparator = context.getGuesses().comparator();

        if (comparator.compare(answer, guess) == 0) {
            res.setGameOver(true);
            res.setLastSubmissionTimestamp(now);
            context.setLastSubmissionTimestamp(now);
            context.setState(new GameCompleteState());
        } else {
            if (context.getDictionary().contains(guess)) {
                boolean added = context.getGuesses().addGuess(context.getAnswer(), context.getGuess());
                if (!added) {
                    LOG.warn("Already guessed: {}", guess);
                    res.setError(SubmitError.ALREADY_GUESSED);
                }
                context.setLastSubmissionTimestamp(now);
                res.setLastSubmissionTimestamp(now);

            } else {
                LOG.error("Guessed an invalid word: {}", guess);
                res.setError(SubmitError.INVALID_WORD);
            }
        }
//        res.setGuesses(context.getGuesses());
        LOG.info("{}", context.getGuesses());
        return res;
    }
}
