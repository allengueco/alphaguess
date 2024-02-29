package org.allengueco;

import org.allengueco.game.Guesses;
import org.allengueco.game.states.GameContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.session.MapSession;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
public class StateController {
    private final static Logger LOG = LoggerFactory.getLogger(StateController.class);
    private GameService gameService;

    @Autowired
    private ReactiveMapSessionRepository sessionRepository;

    @GetMapping("/create")
    public Mono<String> createGame(WebSession session) {
        LOG.info("id: {}", session.getId());

        return Mono.just("CREATED: " + session.getId());
    }

    @GetMapping("/submit")
    public Mono<String> submitGuess(WebSession session) {
        gameService.getGame(session.getId());
        return Mono.just("submitted");
    }

    @GetMapping("/session-details")
    public Mono<String> getGameSession(WebSession session) {
        return sessionRepository.findById(session.getId())
                .map(MapSession::getId);
    }
}
