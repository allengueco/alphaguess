package org.allengueco.service;

import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.GameSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@HttpExchange(url = "/api",
        accept = APPLICATION_JSON_VALUE,
        contentType = APPLICATION_JSON_VALUE)
public interface BetaGuessClient {
    @PostExchange("/submit")
    ResponseEntity<GameSession> submitGuess(@RequestBody SubmitRequest request);
}
