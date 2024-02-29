package org.allengueco.game.states;

import org.allengueco.game.Dictionary;
import org.allengueco.game.Guesses;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;

import java.time.Instant;
import java.util.Random;

public class GameStartState implements State {
    Random random;

    TreeSortedSet<String> words;

    /**
     * Initializes the context.
     *
     * @param context
     */
    @Override
    public void doAction(GameContext context) {
        Instant start = Instant.now();
        Dictionary dict = new Dictionary(this.words, this.random);
        Guesses guesses = Guesses.newGuesses();

        String selectedAnswer = dict.randomWord();

        context.setAnswer(selectedAnswer);
        context.setStart(start);
        context.setDictionary(dict);
        context.setGuess("");
        context.setGuesses(guesses);
    }
}
