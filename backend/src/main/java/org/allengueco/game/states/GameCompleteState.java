package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameCompleteState implements State {
    private final Logger LOG = LoggerFactory.getLogger(GameCompleteState.class);

    @Override
    public ActionResult doAction(GameContext context) {
        LOG.info(context.getGuess());
        ActionResult result = new ActionResult();
        result.setGameOver(true);
        result.setGuesses(context.getGuesses());
        return result;
    }
}
