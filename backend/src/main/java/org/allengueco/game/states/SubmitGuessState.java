package org.allengueco.game.states;

import org.allengueco.game.GameSession;
import org.allengueco.game.Guesses;
import org.allengueco.game.Result;
import org.allengueco.game.SubmitError;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
public class SubmitGuessState implements State {
    private final Logger log = LoggerFactory.getLogger(SubmitGuessState.class);

    @Override
    public GameSession updateSession(GameSession session) {
        Instant now = Instant.now();
        String guess = session.guess();

        GameSession.Mutate mutate = session.mutate();

        log.info("GUESSING: {}", session.guess());
        AddResult res = addGuess(session, guess);

        switch (res.error) {
            case EQUAL -> mutate
                    .withLastSubmissionTimestamp(now)
                    .withState(GameSession.State.Complete)
                    .withError(null)
                    .withIsGameOver(true);
            case ALREADY_GUESSED -> {
                log.warn("Already guessed: {}", guess);
                mutate.withError(SubmitError.ALREADY_GUESSED);
            }
            case ADDED -> mutate.withLastSubmissionTimestamp(now)
                    .withError(null)
                    .withGuesses(res.result);
        }
        return mutate.build();
    }

    /**
     * Attempts to add the guess to the current specified session. If guess == session's answer, returns the session's {@link Guesses} unmodified.
     * Else, attempt to add to either before or after guesses. If guess is added, we return a copy of the session's {@link Guesses} with the new addition -
     * Otherwise, the guess have been guessed before.
     *
     * @param session current {@link GameSession} to add to.
     * @param guess   user's guess
     */
    private AddResult addGuess(GameSession session, String guess) throws IllegalStateException {
        String answer = session.answer();
        Guesses g = session.guesses();

        int result = String.CASE_INSENSITIVE_ORDER.compare(answer, guess);
        if (result == 0) {
            return new AddResult(g, Result.EQUAL);
        }

        Set<String> before = TreeSortedSet.newSet(String.CASE_INSENSITIVE_ORDER, g.before());
        Set<String> after = TreeSortedSet.newSet(String.CASE_INSENSITIVE_ORDER, g.after());
        Set<String> half = result > 0 ? after : before;
        boolean added = half.add(guess);
        Guesses modified = new Guesses(before, after);

        return added ? new AddResult(modified, Result.ADDED) : new AddResult(null, Result.ALREADY_GUESSED);
    }

    private record AddResult(Guesses result, Result error) {

    }
}
