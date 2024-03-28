package org.allengueco.config;

import org.allengueco.commands.Submit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.EnableCommand;

@EnableCommand(Submit.class)
@ConditionalOnProperty(value = "cli")
@Configuration(proxyBeanMethods = false)
public class CliConfig {

}
