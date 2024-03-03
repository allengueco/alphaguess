package org.allengueco.dto;

import org.allengueco.game.Guesses;
import org.allengueco.game.SubmitError;

import java.time.Instant;

public class ActionResult {
    SubmitError error;
    Guesses guesses;
    boolean isGameOver;
    boolean isSessionExpired;
    Instant submissionTimestamp;

    public ActionResult() {
    }

    public static ActionResult defaultResult() {
        ActionResult res = new ActionResult();
        res.setGuesses(Guesses.newGuesses());
        res.setSessionExpired(false);
        res.setSubmissionTimestamp(Instant.MIN);
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

    public boolean isSessionExpired() {
        return isSessionExpired;
    }

    public void setSessionExpired(boolean sessionExpired) {
        isSessionExpired = sessionExpired;
    }

    public Instant getSubmissionTimestamp() {
        return submissionTimestamp;
    }

    public void setSubmissionTimestamp(Instant submissionTimestamp) {
        this.submissionTimestamp = submissionTimestamp;
    }
}
