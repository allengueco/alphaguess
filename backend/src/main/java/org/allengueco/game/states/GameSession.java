package org.allengueco.game.states;

import org.allengueco.game.Guesses;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;
import java.util.Objects;

@RedisHash
public class GameSession {
    @Id
    String id;
    private State state;
    private String answer;
    private String guess;
    private Guesses guesses;
    @CreatedDate
    private Instant start;
    @LastModifiedDate
    private Instant lastSubmissionTimestamp;

    public GameSession() {
    }

    private GameSession(String id) {
        this.id = id;
    }

    public static GameSession withId(String id) {
        return new GameSession(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public Guesses getGuesses() {
        return guesses;
    }

    public void setGuesses(Guesses guesses) {
        this.guesses = guesses;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getLastSubmissionTimestamp() {
        return lastSubmissionTimestamp;
    }

    public void setLastSubmissionTimestamp(Instant lastSubmissionTimestamp) {
        this.lastSubmissionTimestamp = lastSubmissionTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSession that = (GameSession) o;
        return Objects.equals(id, that.id) && getState() == that.getState() && Objects.equals(getAnswer(), that.getAnswer()) && Objects.equals(getGuess(), that.getGuess()) && Objects.equals(getGuesses(), that.getGuesses()) && Objects.equals(getStart(), that.getStart()) && Objects.equals(getLastSubmissionTimestamp(), that.getLastSubmissionTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getState(), getAnswer(), getGuess(), getGuesses(), getStart(), getLastSubmissionTimestamp());
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "id='" + id + '\'' +
                ", state=" + state +
                ", answer='" + answer + '\'' +
                ", guess='" + guess + '\'' +
                ", guesses=" + guesses +
                ", start=" + start +
                ", lastSubmissionTimestamp=" + lastSubmissionTimestamp +
                '}';
    }

    public enum State {
        Initialize, Submit, Complete
    }
}
