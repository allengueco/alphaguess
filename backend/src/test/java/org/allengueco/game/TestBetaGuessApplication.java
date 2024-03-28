package org.allengueco.game;

import org.allengueco.BetaGuessApplication;
import org.allengueco.ContainersConfig;
import org.springframework.boot.SpringApplication;

public class TestBetaGuessApplication {
    public static void main(String[] args) {
        SpringApplication.from(BetaGuessApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
