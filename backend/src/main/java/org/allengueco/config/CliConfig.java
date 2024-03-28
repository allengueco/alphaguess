package org.allengueco.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.annotation.EnableCommand;

@EnableCommand
@ConditionalOnProperty(value = "cli", havingValue = "true")
public class CliConfig {
    @Bean
    CommandRegistration commandRegistration() {
        return CommandRegistration.builder()
                .command("guess")
                .withTarget()
                .consumer(ctx -> ctx.getTerminal().writer().println(""))
                .and()
                .build();
    }
    
}
