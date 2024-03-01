package org.allengueco.game;

import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class AlphaGuessConfig {
    private final Logger LOG = LoggerFactory.getLogger(AlphaGuessConfig.class);

    @Bean
    Dictionary englishDictionary(
            Random random,
            @Value("classpath:words_alpha.txt") Resource wordResource) throws IOException {

        var words = TreeSortedSet.newSet(Files.readAllLines(wordResource.getFile().toPath()));

        return new Dictionary(words, random);
    }

    @Bean
    Random random() {
        return new Random(); //thread local random?
    }


}
