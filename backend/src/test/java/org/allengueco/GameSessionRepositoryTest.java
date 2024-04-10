package org.allengueco;

import org.allengueco.game.GameSession;
import org.allengueco.game.Guesses;
import org.allengueco.repository.GameRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
@ImportTestcontainers(Containers.class)
public class GameSessionRepositoryTest {
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
