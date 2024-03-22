package org.allengueco.game.states;

import org.allengueco.game.GameSession;

@FunctionalInterface
public interface State {
    /**
     * Updates the session based on the implementation.
     *
     * @param session current game session details.
     * @return the result of the mutation.
     */
    GameSession updateSession(GameSession session);
}
