package org.allengueco.game;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class GuessListJsonTest {
    private final Logger log = LoggerFactory.getLogger(GuessListJsonTest.class);
    @Autowired
    JacksonTester<List<Guess>> json;

    @Test
    void serializeGuesses() throws IOException {
        List<Guess> g = new ArrayList<>();

        var content = json.write(g);

        assertThat(content)
                .extractingJsonPathArrayValue("$.before").isEmpty();

        assertThat(content)
                .extractingJsonPathArrayValue("$.after").isEmpty();
    }

    @Test
    void serializeContainsGuesses() throws IOException {
        List<Guess> g = new ArrayList<>();

        var content = json.write(g);

        assertThat(content)
                .extractingJsonPathArrayValue("$.before").contains("base");
    }

    @Test
    void deserializeGuesses() throws IOException {
        String g = """
                {
                    "before" : [],
                    "after" : []
                }
                """;

        var content = json.parse(g);

        assertThat(content)
                .hasNoNullFieldsOrProperties();

//        assertThat(content)
//                .extracting(Guesses::before)
//                .asInstanceOf(InstanceOfAssertFactories.iterable(String.class))
//                .isEmpty();
//
//        assertThat(content)
//                .extracting(Guesses::after)
//                .asInstanceOf(InstanceOfAssertFactories.iterable(String.class))
//                .isEmpty();
    }

    @Test
    void deserializeContainsGuesses() throws IOException {
        String g = """
                {
                    "before" : ["mandarin", "power"],
                    "after" : ["apple", "boy"]
                }
                """;

        var content = json.parse(g);

        assertThat(content)
                .hasNoNullFieldsOrProperties();

//        assertThat(content)
//                .extracting(Guesses::after)
//                .asInstanceOf(InstanceOfAssertFactories.iterable(String.class))
//                .containsExactlyElementsOf(List.of("apple", "boy"));
//
//        assertThat(content)
//                .extracting(Guesses::before)
//                .asInstanceOf(InstanceOfAssertFactories.iterable(String.class))
//                .containsExactlyElementsOf(List.of("mandarin", "power"));
    }
}