package org.allengueco.game.states;

public class SubmitGuessState implements State {
    @Override
    public void doAction(GameContext context) {
        context.submitGuess();
    }
}
