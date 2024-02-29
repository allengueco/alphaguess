package org.allengueco;

import org.allengueco.game.Guesses;
import org.allengueco.game.states.GameContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class StateController {
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameDetail> createGame() {

    }

    private class GameDetail {
        String id;
        Guesses guesses;
        Instant start;

        public GameDetail(String id, Guesses guesses, Instant start) {
            this.id = id;
            this.guesses = guesses;
            this.start = start;
        }
        static GameDetail fromGameContext(GameContext context) {
            return new GameDetail()
        }
    }
}
