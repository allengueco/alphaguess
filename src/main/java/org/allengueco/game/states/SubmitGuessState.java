package org.allengueco.game.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class SubmitGuessState implements State {
    private final Logger LOG = LoggerFactory.getLogger(SubmitGuessState.class);
    @Override
    public void doAction(GameContext context) {
        String answer = context.getAnswer();
        String guess = context.getGuess();
        LOG.info("GUESSING: {}", context.getGuess());
        Comparator<String> comparator = context.getGuesses().comparator();

        if (comparator.compare(answer, guess) == 0) {
            context.setState(new GameCompleteState());
        } else {
            if (context.getDictionary().contains(guess)) {
                context.getGuesses().addGuess(context.getAnswer(), context.getGuess());
            }
        }

        LOG.info("{}", context.getGuesses());
    }
}
