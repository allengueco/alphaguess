package org.allengueco;

import jakarta.servlet.http.HttpSession;
import org.allengueco.dto.ActionResult;
import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.Dictionary;
import org.allengueco.game.WordSelector;
import org.allengueco.game.states.GameContext;
import org.allengueco.game.states.InitializeGameState;
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
    private GameService gameService;

    @Autowired
    private Dictionary dictionary;

    @Autowired
    private WordSelector wordSelector;

    @PostMapping(path = "/submit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ActionResult> submitGuess(
            HttpSession session,
            @RequestBody(required = false) SubmitRequest request) {
        String guess = request == null ? null : request.guess();
        GameContext context = gameService.getGame(session.getId());
        if (context == null) {
            context = GameContext.empty();
            context.setState(new InitializeGameState(dictionary, wordSelector));

            context.doAction(guess); // initializes context
            gameService.addGame(session.getId(), context);

            // the session is started here.
            session.setAttribute("gameId", session.getId());
        }

        return ResponseEntity.ok(context.doAction(guess));
    }
}
