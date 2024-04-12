package org.allengueco.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.Instant;

/**
 * Game Session DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameSessionState {
    @NotNull
    Guesses guesses;
    
    SubmitError error;

    @NotNull
    @PastOrPresent
    Instant lastSubmissionTimestamp;

    @NotNull
    boolean isGameOver;

    public GameSessionState(Guesses guesses, SubmitError error, Instant lastSubmissionTimestamp, boolean isGameOver) {
        this.guesses = guesses;
        this.error = error;
        this.lastSubmissionTimestamp = lastSubmissionTimestamp;
        this.isGameOver = isGameOver;
    }

    public static GameSessionState from(GameSession session) {
        return new GameSessionState(
                Guesses.from(session.guesses()),
                session.error(),
                session.lastSubmissionTimestamp(),
                session.isGameOver()
        );
    }

    @Override
    public String toString() {
        return "GameSessionState{" +
                "guesses=" + guesses +
                ", error=" + error +
                ", lastSubmissionTimestamp=" + lastSubmissionTimestamp +
                ", isGameOver=" + isGameOver +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSessionState that = (GameSessionState) o;
        return isGameOver() == that.isGameOver() && getGuesses().equals(that.getGuesses()) && getError() == that.getError() && getLastSubmissionTimestamp().equals(that.getLastSubmissionTimestamp());
    }

    @Override
    public int hashCode() {
        int result = getGuesses().hashCode();
        result = 31 * result + getError().hashCode();
        result = 31 * result + getLastSubmissionTimestamp().hashCode();
        result = 31 * result + Boolean.hashCode(isGameOver());
        return result;
    }

    public Guesses getGuesses() {
        return guesses;
    }

    public void setGuesses(Guesses guesses) {
        this.guesses = guesses;
    }

    public SubmitError getError() {
        return error;
    }

    public void setError(SubmitError error) {
        this.error = error;
    }

    public Instant getLastSubmissionTimestamp() {
        return lastSubmissionTimestamp;
    }

    public void setLastSubmissionTimestamp(Instant lastSubmissionTimestamp) {
        this.lastSubmissionTimestamp = lastSubmissionTimestamp;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
