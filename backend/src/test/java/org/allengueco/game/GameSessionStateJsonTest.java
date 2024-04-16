package org.allengueco.game;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class GameSessionStateJsonTest {
    private final Logger log = LoggerFactory.getLogger(GameSessionStateJsonTest.class);
    @Autowired
    JacksonTester<GameSessionState> json;

    Guesses guesses = Guesses.empty();

    GameSessionState state = new GameSessionState(guesses, SubmitError.INVALID_WORD, Instant.parse("2020-01-01T00:00:00Z"), false);

    @Test
    void serializeGuesses() throws IOException {
        var content = json.write(state);
        log.info(content.toString());

        assertThat(content).extractingJsonPathArrayValue("$.guesses.before").isEmpty();

        assertThat(content).extractingJsonPathArrayValue("$.guesses.after").isEmpty();
    }

    @Test
    void serializeContainsGuesses() throws IOException {
        List<Guess> g = List.of(new Guess("base", Guess.Position.BEFORE));
        state.setGuesses(Guesses.from(g));

        var content = json.write(state);

        assertThat(content).extractingJsonPathArrayValue("$.guesses.before").contains("base");
    }

    @Test
    void deserializeGuesses() throws IOException {
        String g = """
                {
                    "error": "INVALID_WORD",
                    "lastSubmissionTimestamp": "2020-01-01T00:00:00Z",
                    "gameOver": false,
                    "guesses": {
                        "before" : [],
                        "after" : []
                    }
                }
                """;

        var content = json.parse(g);

        assertThat(content).hasNoNullFieldsOrProperties();

        assertThat(content).extracting(GameSessionState::getGuesses).extracting(Guesses::before).asInstanceOf(InstanceOfAssertFactories.iterable(String.class)).isEmpty();

        assertThat(content).extracting(GameSessionState::getGuesses).extracting(Guesses::after).asInstanceOf(InstanceOfAssertFactories.iterable(String.class)).isEmpty();
    }

    @Test
    void deserializeContainsGuesses() throws IOException {
        String g = """
                {
                    "error": "INVALID_WORD",
                    "lastSubmissionTimestamp": "2020-01-01T00:00:00Z",
                    "gameOver": false,
                    "guesses": {
                        "before" : ["mandarin", "power"],
                        "after" : ["apple", "boy"]
                    }
                }
                """;

        var content = json.parse(g);

        assertThat(content).hasNoNullFieldsOrProperties();

        assertThat(content).extracting(GameSessionState::getGuesses).extracting(Guesses::after).asInstanceOf(InstanceOfAssertFactories.iterable(String.class)).containsExactlyElementsOf(List.of("apple", "boy"));

        assertThat(content).extracting(GameSessionState::getGuesses).extracting(Guesses::before).asInstanceOf(InstanceOfAssertFactories.iterable(String.class)).containsExactlyElementsOf(List.of("mandarin", "power"));
    }

    @Test
    void deserializeNoError() throws IOException {
        {
            String g = """
                    {
                        "lastSubmissionTimestamp": "2020-01-01T00:00:00Z",
                        "gameOver": false,
                        "guesses": {
                            "before" : [],
                            "after" : []
                        }
                    }
                    """;

            var content = json.parse(g);

            assertThat(content)
                    .extracting(GameSessionState::getError)
                    .isNull();

        }
    }
}