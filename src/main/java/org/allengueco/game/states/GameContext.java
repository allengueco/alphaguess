package org.allengueco.game.states;

import org.allengueco.game.Dictionary;
import org.allengueco.game.Guesses;

import java.time.Instant;

public class GameContext {
    private String answer;
    private String guess;
    private Dictionary dictionary;
    private Guesses guesses;
    private Instant start;
    private State currentState;

    public GameContext() {
    }

    public GameContext(String answer,
                       String guess,
                       Dictionary dictionary,
                       Guesses guesses,
                       Instant start) {
        this.answer = answer;
        this.guess = guess;
        this.dictionary = dictionary;
        this.guesses = guesses;
        this.start = start;
    }

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

    public void setStart(Instant start) {
        this.start = start;
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public void doAction(String guess) {
        this.guess = guess;
        currentState.doAction(this);
    }

    public void submitGuess() {
        this.guesses.addGuess(this.answer, this.guess);
    }
}
