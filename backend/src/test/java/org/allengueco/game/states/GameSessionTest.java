package org.allengueco.game.states;

import org.allengueco.game.GameSession;
import org.allengueco.game.Guesses;
import org.allengueco.game.SubmitError;
import org.allengueco.service.GameService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GameSessionTest {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    JacksonTester<GameSession> jackson;

    private static Stream<Arguments> simpleFields() {
        return Stream.concat(valueFields(), timestamps());
    }

    private static Stream<Arguments> valueFields() {
        return Stream.of(
                Arguments.of("id", "1"),
                Arguments.of("answer", "answer"),
                Arguments.of("guess", "guess")
        );
    }

    private static Stream<Arguments> enums() {
        return Stream.of(
                Arguments.of("error", SubmitError.NONE)
        );
    }

    private static Stream<Arguments> booleans() {
        return Stream.of(Arguments.of("isGameOver", false));
    }

    private static Stream<Arguments> timestamps() {
        return Stream.of(
                Arguments.of("start", "1970-01-01T00:00:00Z"),
                Arguments.of("lastSubmissionTimestamp", "+1000000000-12-31T23:59:59.999999999Z"));
    }

    @Nested
    public class Serialization {
        JsonContent<GameSession> asJson;


        @BeforeEach
        void setup() throws IOException {
            Guesses g = new Guesses(
                    TreeSortedSet.newSetWith("mandarin", "power"),
                    TreeSortedSet.newSetWith("base", "case"));
            GameSession s = new GameSession("1",
                    "answer",
                    "guess",
                    GameSession.State.Submit,
                    g,
                    SubmitError.NONE,
                    Instant.EPOCH,
                    Instant.MAX,
                    false);

            asJson = jackson.write(s);
        }

        @ParameterizedTest
        @MethodSource("org.allengueco.game.states.GameSessionTest#simpleFields")
        void valueFieldsAreDefined(String property, String value) {
            assertThat(asJson)
                    .extractingJsonPathStringValue(property)
                    .isEqualToIgnoringCase(value);
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
        ObjectContent<GameSession> content;

        String json;


        @BeforeEach
        void setup() throws IOException {
            json = """
                    {
                        "id" : "1",
                        "answer": "answer",
                        "guess": "guess",
                        "state": "Submit",
                        "start": "1970-01-01T00:00:00Z",
                        "lastSubmissionTimestamp": "+1000000000-12-31T23:59:59.999999999Z",
                        "guesses": {
                            "before": ["base", "case"],
                            "after": ["mandarin", "power"]
                        },
                        "error": "NONE",
                        "isGameOver": false
                    }
                    """;
            content = jackson.parse(json);
        }

        @Test
        void noNullFields() {
            assertThat(content)
                    .hasNoNullFieldsOrProperties();
        }

        @ParameterizedTest
        @MethodSource("org.allengueco.game.states.GameSessionTest#valueFields")
        void valueFieldEquals(String property, String value) {
            assertThat(content)
                    .extracting(property).isEqualTo(value);
        }

        @ParameterizedTest
        @MethodSource("org.allengueco.game.states.GameSessionTest#timestamps")
        void timestampEquals(String property, String value) {
            assertThat(content)
                    .extracting(property)
                    .asInstanceOf(InstanceOfAssertFactories.INSTANT)
                    .isEqualTo(value);
        }
    }
}