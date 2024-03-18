package org.allengueco;

import org.allengueco.game.Guesses;
import org.allengueco.game.states.GameSession;
import org.allengueco.repository.GameRepository;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataRedisTest
//@Testcontainers(disabledWithoutDocker = true)
public class GameSessionIT {
//    @Container
//    @ServiceConnection
//    static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest"));

    @Autowired
    GameRepository repository;

    @Test
    void save() {
        GameSession s = GameSession.withId("1");

        s.setState(GameSession.State.Submit);
        s.setAnswer("answer");
        s.setGuess("guess");
        s.setStart(Instant.EPOCH);
        s.setLastSubmissionTimestamp(Instant.MAX);

        Guesses g = new Guesses();
        g.setAfter(TreeSortedSet.newSetWith("base", "case"));
        g.setBefore(TreeSortedSet.newSetWith("mandarin", "power"));
        s.setGuesses(g);

        repository.save(s);

        Optional<GameSession> retrieved = repository.findById("1");
        assertThat(retrieved)
                .get()
                .hasNoNullFieldsOrProperties()
                .extracting(GameSession::getGuesses)
                .isNotNull();

        assertThat(retrieved)
                .get()
                .extracting(GameSession::getGuesses)
                .extracting(Guesses::getBefore)
                .asList()
                .isNotEmpty();
    }

    @TestConfiguration
    static class GameSessionITConfiguration {
        @Bean
        RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory();
        }
    }
}
