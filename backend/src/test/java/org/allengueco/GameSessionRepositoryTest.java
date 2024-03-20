package org.allengueco;

import com.redis.testcontainers.RedisContainer;
import org.allengueco.game.Guesses;
import org.allengueco.game.states.GameSession;
import org.allengueco.repository.GameRepository;
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
                .containsExactlyElementsOf(List.of("mandarin", "power"));
    }

    @Test
    void saveEmptyGuess() {
        GameSession s = GameSession.withId("1");

        s.setState(GameSession.State.Submit);
        s.setAnswer("answer");
        s.setGuess("guess");
        s.setStart(Instant.EPOCH);
        s.setLastSubmissionTimestamp(Instant.MAX);

        s.setGuesses(Guesses.empty());

        repository.save(s);
        var retrieved = repository.findById("1");

        assertThat(retrieved)
                .get()
                .hasNoNullFieldsOrProperties()
                .extracting(GameSession::getGuesses)
                .isNotNull();
    }
}
