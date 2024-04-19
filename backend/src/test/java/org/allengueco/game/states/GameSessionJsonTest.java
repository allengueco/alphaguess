package org.allengueco.game.states;

import org.allengueco.game.GameSession;
import org.allengueco.game.GameSessionState;
import org.allengueco.game.Guess;
import org.allengueco.game.SubmitError;
import org.allengueco.service.GameService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GameSessionJsonTest {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    JacksonTester<GameSessionState> jackson;

    private static Stream<Arguments> hiddenFields() {
        return Stream.of(Arguments.of("id"),
                Arguments.of("answer"),
                Arguments.of("guess"),
                Arguments.of("state"),
                Arguments.of("start")
        );
    }

    @Nested
    public class Serialization {
        UUID id = UUID.randomUUID();
        List<Guess> g = List.of(
                new Guess("mandarin", Guess.Position.BEFORE),
                new Guess("power", Guess.Position.BEFORE),
                new Guess("base", Guess.Position.AFTER),
                new Guess("case", Guess.Position.AFTER)
        );
        GameSession s = new GameSession(id,
                "answer",
                "guess",
                GameSession.State.Submit,
                g,
                null,
                Instant.EPOCH,
                Instant.MAX,
                false);
        JsonContent<GameSessionState> asJson;

        @BeforeEach
        void setup() throws IOException {
            asJson = jackson.write(GameSessionState.from(s));
        }

        @ParameterizedTest
        @MethodSource("org.allengueco.game.states.GameSessionJsonTest#hiddenFields")
        void stringFieldsAreSerialized(String property) {
            assertThat(asJson)
                    .doesNotHaveJsonPath(property);
        }

        @Test
        void booleanFieldsAreSerialized() {
            log.info(asJson.getJson());
            assertThat(asJson)
                    .extractingJsonPathBooleanValue("gameOver")
                    .isEqualTo(false);
        }

        @Test
        void whenErrorIsNull_thenExcludeField() {
            assertThat(asJson).doesNotHaveJsonPath("error");
        }

        @Test
        void whenError_thenIncludeField() throws IOException {
            var withError = s.mutate().withError(SubmitError.INVALID_WORD).build();
            assertThat(jackson.write(GameSessionState.from(withError)))
                    .extractingJsonPathStringValue("error")
                    .isEqualTo(SubmitError.INVALID_WORD.toString());
        }

        @Test
        void guessesAreDefined() {
            assertThat(asJson)
                    .extractingJsonPathArrayValue("$.guesses.before")
                    .containsExactlyElementsOf(List.of("mandarin", "power"));
            assertThat(asJson)
                    .extractingJsonPathArrayValue("$.guesses.after")
                    .containsExactlyElementsOf(List.of("base", "case"));
        }
    }

    @Nested
    public class Deserialization {
        ObjectContent<GameSessionState> content;

        String json = """
                {
                    "lastSubmissionTimestamp": "+1000000000-12-31T23:59:59.999999999Z",
                    "guesses": {
                        "before": ["base", "case"],
                        "after": ["mandarin", "power"]
                    },
                    "gameOver": false
                }
                """;

        @BeforeEach
        void setup() throws IOException {
            content = jackson.parse(json);
        }

        @Test
        void noNullFields() {
            assertThat(content)
                    .hasNoNullFieldsOrPropertiesExcept("error");
        }

        @Test
        void gameOverIsDefined() {
            assertThat(content)
                    .extracting(GameSessionState::isGameOver)
                    .isEqualTo(false);
        }

        @Test
        void whenError_thenShouldIncluded() throws IOException {
            String withError = """
                    {
                        "lastSubmissionTimestamp": "+1000000000-12-31T23:59:59.999999999Z",
                        "guesses": {
                            "before": ["base", "case"],
                            "after": ["mandarin", "power"]
                        },
                        "error": "INVALID_WORD",
                        "gameOver": false
                    }
                    """;

            assertThat(jackson.parse(withError))
                    .extracting(GameSessionState::getError)
                    .isEqualTo(SubmitError.INVALID_WORD);
        }

        @Test
        void timestampEquals() {
            assertThat(content)
                    .extracting(GameSessionState::getLastSubmissionTimestamp)
                    .asInstanceOf(InstanceOfAssertFactories.INSTANT)
                    .isEqualTo("+1000000000-12-31T23:59:59.999999999Z");
        }
    }
}