package org.allengueco.config;

import org.allengueco.game.Dictionary;
import org.allengueco.game.DictionaryImpl;
import org.allengueco.game.WordSelector;
import org.allengueco.game.WordSelectorImpl;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

@Configuration
@EnableJpaRepositories(basePackages = "org.allengueco.repository")
public class BetaGuessConfig {
    private final Logger log = LoggerFactory.getLogger(BetaGuessConfig.class);

    @Bean
    Dictionary englishDictionary(@Value("classpath:dictionary.txt") Resource dictionaryResource) throws IOException {
        var words = TreeSortedSet.newSet(new BufferedReader(new InputStreamReader(dictionaryResource.getInputStream())).lines().toList());

        return new DictionaryImpl(words);
    }

    @Bean
    WordSelector wordSelector(Random random, @Value("classpath:valid_words.txt") Resource wordResource) throws IOException {
        var words = TreeSortedSet.newSet(new BufferedReader(new InputStreamReader(wordResource.getInputStream())).lines().toList());

        return new WordSelectorImpl(words, random);
    }

    @Bean
    Random random() {
        return new Random(); //thread local random?
    }


}
