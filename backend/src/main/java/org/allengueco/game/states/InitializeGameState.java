package org.allengueco.game.states;

import org.allengueco.game.GameSession;
import org.allengueco.game.WordSelector;
import org.eclipse.collections.api.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InitializeGameState implements State {
    private final Logger log = LoggerFactory.getLogger(InitializeGameState.class);

    WordSelector wordSelector;

    public InitializeGameState(WordSelector wordSelector) {
        this.wordSelector = wordSelector;
    }

    @Override
    public GameSession updateSession(GameSession session) {
        String selectedWord = wordSelector.randomWord();
        log.info("selected answer: {}", selectedWord);
        return session
                .mutate()
                .withAnswer(selectedWord)
                .withGuesses(Lists.mutable.empty())
                .withStart(Instant.now())
                .withState(GameSession.State.Submit)
                .withError(null)
                .withIsGameOver(false)
                .build();
    }
}
