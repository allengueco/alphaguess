package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameCompleteState implements State {
    private final Logger log = LoggerFactory.getLogger(GameCompleteState.class);

    @Override
    public ActionResult updateSession(GameSession session) {
        ActionResult result = ActionResult.defaultResult(session);
        result.setGameOver(true);
        return result;
    }
}
