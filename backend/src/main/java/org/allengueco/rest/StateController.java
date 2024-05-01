package org.allengueco.rest;

import jakarta.servlet.http.HttpSession;
import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.GameSessionState;
import org.allengueco.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class StateController {
    private final static Logger log = LoggerFactory.getLogger(StateController.class);

    final GameService gameService;

    public StateController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path = "/submit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GameSessionState> submitGuess(
            HttpSession session,
            @RequestBody(required = false) SubmitRequest request) {
        String guess = request == null ? null : request.guess();
        return ResponseEntity.of(gameService.addGuess(UUID.fromString(session.getId()), guess)
                .map(GameSessionState::from));

    }

    @GetMapping(path = "/word-of-the-day")
    public ResponseEntity<WordOfTheDay> getWordOfTheDay(HttpRequest request) {
        return null;
    }

    public record WordOfTheDay(String word, Instant requestTimeStamp) {
    }
}
