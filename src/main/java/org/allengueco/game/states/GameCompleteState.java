package org.allengueco.game.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameCompleteState implements State {
    private final Logger LOG = LoggerFactory.getLogger(GameCompleteState.class);
    @Override
    public void doAction(GameContext context) {
        LOG.info("GAME COMPLETE");
    }
}
