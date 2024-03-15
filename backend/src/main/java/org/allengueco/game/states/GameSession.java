package org.allengueco.game.states;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.allengueco.dto.ActionResult;
import org.allengueco.game.Guesses;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@RedisHash
public class GameSession {
    @Id
    String id;
    private String answer;
    private String guess;
    private Guesses guesses;
    @CreatedDate
    private Instant start;
    @LastModifiedDate
    private Instant lastSubmissionTimestamp;
    @JsonIgnore
    @Transient
    private State currentState;

    private GameSession(String id) {
        this.id = id;
    }

    public static GameSession withId(String id) {
        return new GameSession(id);
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

    public void setState(State state) {
        this.currentState = state;
    }

    public ActionResult updateSession(String guess) {
        this.setGuess(guess);
        return currentState.doAction(this);
    }

    public Instant getLastSubmissionTimestamp() {
        return lastSubmissionTimestamp;
    }

    public void setLastSubmissionTimestamp(Instant lastSubmissionTimestamp) {
        this.lastSubmissionTimestamp = lastSubmissionTimestamp;
    }
}
