package org.allengueco.game.states;

import org.allengueco.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

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
                    .withGuesses(res.guesses);
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
        List<Guess> guesses = session.guesses();

        int result = String.CASE_INSENSITIVE_ORDER.compare(answer, guess);
        if (result == 0) {
            return new AddResult(guesses, Result.EQUAL);
        }

        if (guesses.stream().anyMatch(g -> g.getWord().equals(guess))) {
            return new AddResult(guesses, Result.ALREADY_GUESSED);
        }

        Guess.Position pos = result > 0 ? Guess.Position.AFTER : Guess.Position.BEFORE;
        Guess addedGuess = new Guess(guess, pos);
        guesses.add(addedGuess);

        return new AddResult(guesses, Result.ADDED);
    }

    private record AddResult(List<Guess> guesses, Result error) {

    }
}
