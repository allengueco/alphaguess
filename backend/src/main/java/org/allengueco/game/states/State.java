package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;

public interface State {
    ActionResult doAction(GameSession session);
}
