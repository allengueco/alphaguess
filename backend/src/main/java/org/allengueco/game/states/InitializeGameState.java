package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.Dictionary;
import org.allengueco.game.Guesses;
import org.allengueco.game.WordSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class InitializeGameState implements State {
    private final Logger LOG = LoggerFactory.getLogger(InitializeGameState.class);
    Dictionary dictionary;

    WordSelector wordSelector;

    public InitializeGameState(Dictionary dict, WordSelector wordSelector) {
        this.dictionary = dict;
        this.wordSelector = wordSelector;
    }

    @Override
    public ActionResult doAction(GameContext context) {
        ActionResult res = ActionResult.defaultResult(context);
        String selectedWord = wordSelector.randomWord();
        LOG.info("selected answer: {}", selectedWord);

        context.setAnswer(selectedWord);
        context.setGuesses(Guesses.newGuesses());
        context.setStart(Instant.now());
        context.setDictionary(dictionary);

        LOG.info("INITIALIZED");

        context.setState(new SubmitGuessState());
        return res;
    }
}
