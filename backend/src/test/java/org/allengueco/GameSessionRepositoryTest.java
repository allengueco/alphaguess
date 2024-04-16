package org.allengueco;

import org.allengueco.game.GameSession;
import org.allengueco.game.Guess;
import org.allengueco.repository.GameSessionRepository;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ImportTestcontainers(Containers.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameSessionRepositoryTest {
    private final Instant START = Instant.parse("2020-01-01T00:00:00Z");
    private final Instant SUBMIT_TIMESTAMP = Instant.parse("2020-01-02T00:00:00Z");
    @Autowired
    GameSessionRepository repository;

    @Test
    void save() {
        List<Guess> g = List.of(
                new Guess("mandarin", Guess.Position.BEFORE),
                new Guess("power", Guess.Position.BEFORE),
                new Guess("base", Guess.Position.AFTER),
                new Guess("case", Guess.Position.AFTER)
        );
        GameSession s = new GameSession("1",
                "answer",
                "guess",
                GameSession.State.Submit,
                g,
                null,
                START,
                SUBMIT_TIMESTAMP,
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
                .asInstanceOf(InstanceOfAssertFactories.iterable(Guess.class))
                .filteredOn(gu -> gu.getPosition() == Guess.Position.BEFORE)
                .extracting(Guess::getWord)
                .containsExactlyElementsOf(List.of("mandarin", "power"));
    }

    @Test
    void saveEmptyGuess() {
        List<Guess> g = List.of(new Guess("boy", Guess.Position.BEFORE));
        GameSession s = new GameSession("1",
                "answer",
                "guess",
                GameSession.State.Submit,
                g,
                null,
                START,
                SUBMIT_TIMESTAMP,
                false);

        repository.save(s);
        var retrieved = repository.findById("1");

        assertThat(retrieved)
                .get()
                .hasNoNullFieldsOrPropertiesExcept("error")
                .extracting(GameSession::guesses, as(InstanceOfAssertFactories.list(Guess.class)))
                .isNotEmpty();
    }

    @Test
    void addGuessTest() {
        List<Guess> g = List.of(
                new Guess("mandarin", Guess.Position.BEFORE),
                new Guess("power", Guess.Position.BEFORE),
                new Guess("base", Guess.Position.AFTER),
                new Guess("case", Guess.Position.AFTER)
        );
        GameSession s = new GameSession("1",
                "answer",
                "guess",
                GameSession.State.Submit,
                g,
                null,
                START,
                SUBMIT_TIMESTAMP,
                false);

        repository.save(s);

        var retrieved = repository.findById("1");
        retrieved.ifPresent(gs -> gs.addGuess(new Guess("zip", Guess.Position.BEFORE)));

        assertThat(retrieved)
                .get().extracting(GameSession::guesses, as(InstanceOfAssertFactories.list(Guess.class)))
                .anyMatch(gs -> gs.getWord().equals("zip") && gs.getPosition().equals(Guess.Position.BEFORE))
                .hasSize(5);

    }
}
