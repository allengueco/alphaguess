package org.allengueco.game.states;

import org.allengueco.game.GameSession;
import org.allengueco.game.Guesses;
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
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GameSessionTest {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    JacksonTester<GameSession> jackson;

    private static Stream<Arguments> simpleFields() {
        return Stream.of(stringFields(), timestamps()).flatMap(Function.identity());
    }

    private static Stream<Arguments> stringFields() {
        return Stream.of(
                Arguments.of("id", "1"),
                Arguments.of("answer", "answer"),
                Arguments.of("guess", "guess")
        );
    }

    private static Stream<Arguments> timestamps() {
        return Stream.of(
                Arguments.of("start", "1970-01-01T00:00:00Z"),
                Arguments.of("lastSubmissionTimestamp", "+1000000000-12-31T23:59:59.999999999Z"));
    }

    @Nested
    public class Serialization {
        Guesses g = new Guesses(
                TreeSortedSet.newSetWith("mandarin", "power"),
                TreeSortedSet.newSetWith("base", "case"));
        GameSession s = new GameSession("1",
                "answer",
                "guess",
                GameSession.State.Submit,
                null,
                null,
                Instant.EPOCH,
                Instant.MAX,
                false);
        JsonContent<GameSession> asJson;
        JsonContent<GameSession> asSummaryJson;

        @BeforeEach
        void setup() throws IOException {
            asJson = jackson.write(s);
            asSummaryJson = jackson.forView(GameSession.Summary.class).write(s);
        }

        @ParameterizedTest
        @MethodSource("org.allengueco.game.states.GameSessionTest#simpleFields")
        void stringFieldsAreSerialized(String property, String value) {
            assertThat(asJson)
                    .extractingJsonPathStringValue(property)
                    .isEqualToIgnoringCase(value);
        }

        @Test
        void booleanFieldsAreSerialized() {
            assertThat(asJson)
                    .extractingJsonPathBooleanValue("isGameOver")
                    .isEqualTo(false);
        }

        @Test
        void enumFieldsAreSerialized() {
            assertThat(asJson)
                    .extractingJsonPathStringValue("error")
                    .isNull();
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

        @Test
        void withSummaryView() {
            assertThat(asSummaryJson)
                    .hasJsonPath("$.guesses")
                    .hasJsonPath("$.error")
                    .hasJsonPath("$.lastSubmissionTimestamp")
                    .hasJsonPath("$.isGameOver")
                    .doesNotHaveJsonPath("$.id")
                    .doesNotHaveJsonPath("$.answer")
                    .doesNotHaveJsonPath("$.guess")
                    .doesNotHaveJsonPath("$.state")
                    .doesNotHaveJsonPath("$.start");
        }
    }

    @Nested
    public class Deserialization {
        ObjectContent<GameSession> content;
        ObjectContent<GameSession> asSummaryContent;

        String json = """
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
                    "error": null,
                    "isGameOver": false
                }
                """;

        @BeforeEach
        void setup() throws IOException {
            content = jackson.parse(json);
            asSummaryContent = jackson.forView(GameSession.Summary.class).parse(json);
        }

        @Test
        void noNullFields() {
            assertThat(content)
                    .hasNoNullFieldsOrPropertiesExcept("error");
        }

        @ParameterizedTest
        @MethodSource("org.allengueco.game.states.GameSessionTest#stringFields")
        void stringFieldsAreDeserialized(String property, String value) {
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

        @Test
        void withSummaryView() {
            assertThat(asSummaryContent)
                    .hasAllNullFieldsOrPropertiesExcept("guesses", "error", "lastSubmissionTimestamp", "isGameOver");
        }
    }
}