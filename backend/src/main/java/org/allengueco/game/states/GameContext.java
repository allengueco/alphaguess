package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.Dictionary;
import org.allengueco.game.Guesses;

import java.time.Instant;

public class GameContext {
    private String answer;
    private String guess;
    private Dictionary dictionary;
    private Guesses guesses;
    private Instant start;
    private Instant lastSubmissionTimestamp;
    private State currentState;

    public static GameContext empty() {
        return new GameContext();
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

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
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

    public ActionResult doAction(String guess) {
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
