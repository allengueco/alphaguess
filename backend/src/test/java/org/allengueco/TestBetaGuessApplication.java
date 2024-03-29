package org.allengueco;

import org.springframework.boot.SpringApplication;

/**
 * This is what gets booted up during `mvn spring-boot:test-run`
 */
public class TestBetaGuessApplication {
    public static void main(String[] args) {
        SpringApplication.from(BetaGuessApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
