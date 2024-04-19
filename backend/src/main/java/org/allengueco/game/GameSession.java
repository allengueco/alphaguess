package org.allengueco.game;

import jakarta.persistence.*;
import org.eclipse.collections.api.factory.Lists;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "session")
public final class GameSession {
    @Id
    private UUID id;

    private String answer;

    private String guess;

    private State state;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "session_id")
    private List<Guess> guesses = Lists.mutable.empty();

    private SubmitError error;

    @CreatedDate
    private Instant start;

    @LastModifiedDate
    private Instant lastSubmissionTimestamp;

    private boolean isGameOver;

    public GameSession(UUID id, String answer, String guess, State state, List<Guess> guesses, SubmitError error, Instant start, Instant lastSubmissionTimestamp, boolean isGameOver) {
        this.id = id;
        this.answer = answer;
        this.guess = guess;
        this.state = state;
        this.guesses = guesses;
        this.error = error;
        this.start = start;
        this.lastSubmissionTimestamp = lastSubmissionTimestamp;
        this.isGameOver = isGameOver;
    }

    public GameSession() {

    }

    public void addGuess(Guess guess) {
        this.guesses.add(guess);
        guess.setSession(this);
    }

    public Mutate mutate() {
        return new Mutate(this);
    }

    @Override
    public String toString() {
        return "GameSession{" + "id='" + id + '\'' + ", state=" + state + ", answer='" + answer + '\'' + ", guesses=" + guesses + ", start=" + start + ", lastSubmissionTimestamp=" + lastSubmissionTimestamp + '}';
    }

    public UUID id() {
        return id;
    }

    public String answer() {
        return answer;
    }

    public String guess() {
        return guess;
    }

    public State state() {
        return state;
    }

    public List<Guess> guesses() {
        return guesses;
    }

    public SubmitError error() {
        return error;
    }

    public Instant start() {
        return start;
    }

    public Instant lastSubmissionTimestamp() {
        return lastSubmissionTimestamp;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GameSession) obj;
        return Objects.equals(this.id, that.id) && Objects.equals(this.answer, that.answer) && Objects.equals(this.guess, that.guess) && Objects.equals(this.state, that.state) && Objects.equals(this.guesses, that.guesses) && Objects.equals(this.error, that.error) && Objects.equals(this.start, that.start) && Objects.equals(this.lastSubmissionTimestamp, that.lastSubmissionTimestamp) && this.isGameOver == that.isGameOver;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer, guess, state, guesses, error, start, lastSubmissionTimestamp, isGameOver);
    }

    public enum State {
        Initialize, Submit, Complete
    }

    /**
     * Not really mutate since I want to keep {@link GameSession} immutable. I copy over the fields and make changes
     */
    public static class Mutate {
        UUID id;
        State state;
        String answer;
        String guess;
        List<Guess> guesses;
        SubmitError error;
        Instant start;
        Instant lastSubmissionTimestamp;
        boolean isGameOver;

        public Mutate(UUID id, State state, String answer, String guess, List<Guess> guesses, SubmitError error, Instant start, Instant lastSubmissionTimestamp, boolean isGameOver) {
            this.id = id;
            this.state = state;
            this.answer = answer;
            this.guess = guess;
            this.guesses = guesses;
            this.error = error;
            this.start = start;
            this.lastSubmissionTimestamp = lastSubmissionTimestamp;
            this.isGameOver = isGameOver;
        }

        public Mutate(GameSession s) {
            this(s.id, s.state, s.answer, s.guess, s.guesses, s.error, s.start, s.lastSubmissionTimestamp, s.isGameOver);
        }

        public Mutate withId(UUID id) {
            this.id = id;
            return this;
        }

        public Mutate withState(State state) {
            this.state = state;
            return this;
        }

        public Mutate withAnswer(String answer) {
            this.answer = answer;
            return this;
        }

        public Mutate withGuess(String guess) {
            this.guess = guess;
            return this;
        }

        public Mutate withGuesses(List<Guess> guesses) {
            this.guesses = guesses;
            return this;
        }

        public Mutate withError(SubmitError error) {
            this.error = error;
            return this;
        }

        public Mutate withStart(Instant start) {
            this.start = start;
            return this;
        }

        public Mutate withLastSubmissionTimestamp(Instant lastSubmissionTimestamp) {
            this.lastSubmissionTimestamp = lastSubmissionTimestamp;
            return this;
        }

        public Mutate withIsGameOver(boolean isGameOver) {
            this.isGameOver = isGameOver;
            return this;
        }

        public GameSession build() {
            return new GameSession(this.id, this.answer, this.guess, this.state, this.guesses, this.error, this.start, this.lastSubmissionTimestamp, this.isGameOver);
        }
    }
}
