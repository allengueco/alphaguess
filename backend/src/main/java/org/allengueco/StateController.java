package org.allengueco;

import org.allengueco.dto.ActionResult;
import org.allengueco.game.Dictionary;
import org.allengueco.game.WordSelector;
import org.allengueco.game.states.GameContext;
import org.allengueco.game.states.InitializeGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class StateController {
    private final static Logger LOG = LoggerFactory.getLogger(StateController.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private Dictionary dictionary;

    @Autowired
    private WordSelector wordSelector;

    @PostMapping(path = "/submit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ActionResult> submitGuess(WebSession session, @RequestBody SubmitRequest request) {
        GameContext context = gameService.getGame(session.getId());
        if (context == null) {
            context = GameContext.empty();
            context.setState(new InitializeGameState(dictionary, wordSelector));

            context.doAction(request.guess()); // initializes context
            gameService.addGame(session.getId(), context);
            session.getAttributes().put("gameId", session.getId());
        }

        return Mono.just(context.doAction(request.guess()));
    }

    private Mono<ActionResult> updateFromResult(ActionResult result, String gameId) {
        return Mono.just(result)
                .doOnNext(res -> {
                    if (res.isGameOver()) gameService.removeGame(gameId);
                });
    }

    private record SubmitRequest(String guess) {
    }
}
