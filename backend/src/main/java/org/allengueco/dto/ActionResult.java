package org.allengueco.dto;

import org.allengueco.game.Guesses;
import org.allengueco.game.SubmitError;
import org.allengueco.game.states.GameSession;

import java.time.Instant;

public class ActionResult {
    SubmitError error;
    Guesses guesses;
    boolean isGameOver;
    Instant lastSubmissionTimestamp;

    public ActionResult() {
    }

    public static ActionResult defaultResult(GameSession context) {
        ActionResult res = new ActionResult();
        res.setGuesses(context.getGuesses());
        res.setLastSubmissionTimestamp(context.getLastSubmissionTimestamp());
        res.setGameOver(false);
        return res;
    }

    public SubmitError getError() {
        return error;
    }

    public void setError(SubmitError error) {
        this.error = error;
    }

    public Guesses getGuesses() {
        return guesses;
    }

    public void setGuesses(Guesses guesses) {
        this.guesses = guesses;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public Instant getLastSubmissionTimestamp() {
        return lastSubmissionTimestamp;
    }

    public void setLastSubmissionTimestamp(Instant lastSubmissionTimestamp) {
        this.lastSubmissionTimestamp = lastSubmissionTimestamp;
    }
}
