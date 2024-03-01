package org.allengueco;

import org.allengueco.dto.SubmissionSummary;
import org.allengueco.game.Dictionary;
import org.allengueco.game.states.GameContext;
import org.allengueco.game.states.InitializeGameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.MapSession;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.web.bind.annotation.CookieValue;
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
    private ReactiveMapSessionRepository sessionRepository;

    @GetMapping("/create")
    public Mono<String> createGame(WebSession session) {
        LOG.info("id: {}", session.getId());

        return Mono.just("CREATED: " + session.getId());
    }


    @GetMapping("/submit")
    public Mono<String> submitGuess(WebSession session, @RequestParam String guess) {
        GameContext context = gameService.getGame(session.getId());
        if (context == null) {
            context = GameContext.empty();
            context.setState(new InitializeGameState(this.dictionary));

            context.doAction(guess); // initializes context
            gameService.addGame(session.getId(), context);
        }
        session.start();
        context.doAction(guess);

        return Mono.just("submitted");
    }

    @GetMapping("/session-details")
    public Mono<String> getGameSession(WebSession session) {
        return sessionRepository.findById(session.getId())
                .map(MapSession::getId);
    }
}
