package org.allengueco.game.states;

import com.fasterxml.jackson.annotation.JsonView;
import org.allengueco.game.Guesses;
import org.allengueco.game.SubmitError;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@RedisHash
public record GameSession(@Id String id,
                          String answer,
                          String guess,
                          State state,
                          @JsonView(Summary.class)
                          Guesses guesses,
                          @JsonView(Summary.class)
                          SubmitError error,

                          @CreatedDate
                          Instant start,
                          @JsonView(Summary.class)
                          @LastModifiedDate
                          Instant lastSubmissionTimestamp,
                          @JsonView(Summary.class)
                          boolean isGameOver) {

    public Mutate mutate() {
        return new Mutate(this);
    }

    @Override
    public String toString() {
        return "GameSession{" + "id='" + id + '\'' + ", state=" + state + ", answer='" + answer + '\'' + ", guess='" + guess + '\'' + ", guesses=" + guesses + ", start=" + start + ", lastSubmissionTimestamp=" + lastSubmissionTimestamp + '}';
    }

    public enum State {
        Initialize, Submit, Complete
    }

    public static class Summary {

    }

    /**
     * Not really modify since I want to keep {@link GameSession} immutable. I copy over the fields and make changes
     */
    public static class Mutate {
        String id;
        State state;
        String answer;
        String guess;
        Guesses guesses;
        SubmitError error;
        Instant start;
        Instant lastSubmissionTimestamp;
        boolean isGameOver;

        public Mutate(String id, State state, String answer, String guess, Guesses guesses, SubmitError error, Instant start, Instant lastSubmissionTimestamp, boolean isGameOver) {
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

        public Mutate withId(String id) {
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

        public Mutate withGuesses(Guesses guesses) {
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
            return new GameSession(this.id, this.guess, this.answer, this.state, this.guesses, this.error, this.start, this.lastSubmissionTimestamp, this.isGameOver);
        }
    }
}
