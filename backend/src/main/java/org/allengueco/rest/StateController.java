package org.allengueco.rest;

import jakarta.servlet.http.HttpSession;
import org.allengueco.dto.ActionResult;
import org.allengueco.dto.SubmitRequest;
import org.allengueco.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StateController {
    private final static Logger LOG = LoggerFactory.getLogger(StateController.class);

    @Autowired
    GameService gameService;

    @PostMapping(path = "/submit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ActionResult> submitGuess(
            HttpSession session,
            @RequestBody(required = false) SubmitRequest request) {
        String guess = request == null ? null : request.guess();
        return ResponseEntity.of(gameService.addGuess(session.getId(), guess));
    }
}
