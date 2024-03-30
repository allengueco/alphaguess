package org.allengueco;

import com.redis.testcontainers.RedisContainer;
import org.allengueco.game.GameSession;
import org.allengueco.game.Guesses;
import org.allengueco.repository.GameRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataRedisTest
@Testcontainers(disabledWithoutDocker = true)
public class GameSessionRepositoryTest {
    @Container
    @ServiceConnection
    static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest"));

    @Autowired
    GameRepository repository;

    @Test
    void save() {
        Guesses g = new Guesses(
                TreeSortedSet.newSetWith("mandarin", "power"),
                TreeSortedSet.newSetWith("base", "case"));
        GameSession s = new GameSession("1",
                "answer",
                "guess",
                GameSession.State.Submit,
                g,
                null,
                Instant.EPOCH,
                Instant.MAX,
                false);

        repository.save(s);

        Optional<GameSession> retrieved = repository.findById("1");
        assertThat(retrieved)
                .get()
                .hasNoNullFieldsOrPropertiesExcept("error")
                .extracting(GameSession::guesses)
                .isNotNull();

        assertThat(retrieved)
                .get()
                .extracting(GameSession::guesses)
                .extracting(Guesses::before)
                .asInstanceOf(InstanceOfAssertFactories.iterable(String.class))
                .containsExactlyElementsOf(List.of("mandarin", "power"));
    }

    @Test
    void saveEmptyGuess() {
        Guesses g = new Guesses(
                TreeSortedSet.newSetWith("boy"),
                TreeSortedSet.newSetWith());
        GameSession s = new GameSession("1",
                "answer",
                "guess",
                GameSession.State.Submit,
                g,
                null,
                Instant.EPOCH,
                Instant.MAX,
                false);

        repository.save(s);
        var retrieved = repository.findById("1");

        assertThat(retrieved)
                .get()
                .hasNoNullFieldsOrPropertiesExcept("error")
                .extracting(GameSession::guesses)
                .isNotNull();
    }
}
