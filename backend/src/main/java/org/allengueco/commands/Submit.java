package org.allengueco.commands;

import org.allengueco.game.Dictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.stereotype.Component;

@Command
@Component
public class Submit {
    private static final Logger log = LoggerFactory.getLogger(Submit.class);
    @Autowired
    Dictionary dictionary;

    @Command(command = "guess")
    public String guess(@Option(required = true) String guess) {
        log.info("contains: {}", dictionary.contains(guess));
        return "You guess: %s".formatted(guess);
    }
}
