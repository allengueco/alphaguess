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

import java.util.Iterator;

@RestController
@RequestMapping("/api")
public class StateController {
    private final static Logger LOG = LoggerFactory.getLogger(StateController.class);

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
        LOG.info("id: {}", session.getId());
        for (Iterator<String> it = session.getAttributeNames().asIterator(); it.hasNext(); ) {
            var e = it.next();
            LOG.info("attr: {}", e);
        }
        String guess = request == null ? null : request.guess();
        if (session.isNew()) {
            LOG.info("Initializing new session with id: {}...", session.getId());
            GameContext newContext = GameContext.empty();
            newContext.setState(new InitializeGameState(dictionary, wordSelector));

            newContext.doAction(guess); // initializes context

            session.setAttribute("context", newContext);
        }

        GameContext context = (GameContext) session.getAttribute("context");

        return ResponseEntity.ok(context.doAction(guess));
    }
}
