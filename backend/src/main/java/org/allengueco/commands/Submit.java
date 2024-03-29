package org.allengueco.commands;

import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.GameSession;
import org.allengueco.service.BetaGuessClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Command
@Component
public class Submit {
    private static final Logger log = LoggerFactory.getLogger(Submit.class);
    @Autowired
    BetaGuessClient betaGuessClient;
    private String cookie;

    @Command(command = "guess")
    public String guess(@Option(required = true) String guess) {
        var response = betaGuessClient.submitGuess(new SubmitRequest(guess));

        return "You guess: %s".formatted(summary(Objects.requireNonNull(response.getBody())));
    }

    private String summary(GameSession session) {
        final String BEFORE = String.join("\n", session.guesses().before());
        final String AFTER = String.join("\n", session.guesses().after());

        return """
                BEFORE:
                %s
                AFTER :
                %s
                """.formatted(BEFORE, AFTER);
    }
}
