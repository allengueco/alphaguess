package org.allengueco;

import org.allengueco.game.Dictionary;
import org.allengueco.game.WordSelector;
import org.allengueco.game.states.ActionResult;
import org.allengueco.game.states.GameContext;
import org.allengueco.game.states.InitializeGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
public class StateController {
    private final static Logger LOG = LoggerFactory.getLogger(StateController.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private Dictionary dictionary;

    @Autowired
    private WordSelector wordSelector;

    @GetMapping("/submit")
    public Mono<ActionResult> submitGuess(WebSession session, @RequestParam String guess) {
        GameContext context = gameService.getGame(session.getId());
        if (context == null) {
            context = GameContext.empty();
            context.setState(new InitializeGameState(dictionary, wordSelector));

            context.doAction(guess); // initializes context
            gameService.addGame(session.getId(), context);
            session.getAttributes().put("gameId", session.getId());
        }

        String gameId = session.getAttribute("gameId");

        return updateFromResult(context.doAction(guess), gameId);
    }

    /**
     * @param result
     * @return
     */
    private Mono<ActionResult> updateFromResult(ActionResult result, String gameId) {
        return Mono.just(result)
                .doOnNext(res -> {
                    if (res.isGameOver()) gameService.removeGame(gameId);
                });
    }


}
