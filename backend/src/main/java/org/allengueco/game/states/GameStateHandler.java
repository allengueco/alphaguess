package org.allengueco.game.states;

import org.allengueco.dto.ActionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameStateHandler {
    @Autowired
    InitializeGameState initializeGameState;

    @Autowired
    SubmitGuessState submitGuessState;

    @Autowired
    GameCompleteState gameCompleteState;

    public ActionResult handle(GameSession session) {
        State stateHandler = switch (session.getState()) {
            case Initialize -> initializeGameState;
            case Submit -> submitGuessState;
            case Complete -> gameCompleteState;
        };

        return stateHandler.updateSession(session);
    }
}
