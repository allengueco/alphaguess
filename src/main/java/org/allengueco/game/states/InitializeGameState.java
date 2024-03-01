package org.allengueco.game.states;

import org.allengueco.game.Dictionary;
import org.allengueco.game.Guesses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class InitializeGameState implements State {
    private final Logger LOG = LoggerFactory.getLogger(InitializeGameState.class);
    Dictionary dictionary;

    public InitializeGameState(Dictionary dict) {
        this.dictionary = dict;
    }
    @Override
    public void doAction(GameContext context) {
        String selectedWord = dictionary.randomWord();
        LOG.info("selected answer: {}", selectedWord);

        context.setAnswer(selectedWord);
        context.setGuesses(Guesses.newGuesses());
        context.setStart(Instant.now());
        context.setDictionary(dictionary);

        LOG.info("INITIALIZED");

        context.setState(new SubmitGuessState());
    }
}
