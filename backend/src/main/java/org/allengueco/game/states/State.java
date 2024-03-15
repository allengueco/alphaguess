package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;

@FunctionalInterface
public interface State {
    /**
     * Updates the session based on the implementation.
     * @param session current game session details.
     * @return the result of the mutation.
     */
    ActionResult updateSession(GameSession session);
}
