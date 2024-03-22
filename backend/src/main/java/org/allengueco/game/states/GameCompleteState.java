package org.allengueco.game.states;

import org.allengueco.game.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameCompleteState implements State {
    private final Logger log = LoggerFactory.getLogger(GameCompleteState.class);

    @Override
    public GameSession updateSession(GameSession session) {
        return session.mutate()
                .withIsGameOver(true)
                .build();
    }
}
