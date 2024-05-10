package org.allengueco;

import jakarta.servlet.http.Cookie;
import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.Dictionary;
import org.allengueco.game.WordSelector;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
@ImportTestcontainers(Containers.class)
public class GameSessionIT {
    private static final Logger log = LoggerFactory.getLogger(GameSessionIT.class);
    final String SUBMIT_API = "/api/submit";

    @Autowired
    MockMvc mvc;

    @Autowired
    Dictionary dictionary;

    @Autowired
    WordSelector wordSelector;

    @Test
    void whenNoGuessAndNoActiveSession_thenShouldReturnEmptyActionResult() throws Exception {
        when(wordSelector.randomWord()).thenReturn("answer");
        testRequest(null, null)
                .andExpectAll(
                        header().exists("Set-Cookie"),
                        status().isOk(),
                        jsonPath("$.guesses", Matchers.notNullValue()),
                        jsonPath("$.guesses.before", Matchers.empty()),
                        jsonPath("$.guesses.after", Matchers.empty()),
                        jsonPath("$.error").doesNotExist(),
                        jsonPath("$.gameOver", Matchers.is(false)),
                        jsonPath("$.lastSubmissionTimestamp").doesNotExist()
                );
    }

    @Test
    void whenGuessingWithActiveSession_thenShouldReturnCurrentState() throws Exception {
        when(dictionary.contains(anyString())).thenReturn(true);
        when(wordSelector.randomWord()).thenReturn("answer");

        var initRequest = testRequest(new SubmitRequest("great"), null)
                .andExpectAll(
                        status().isOk(),
                        header().exists("Set-Cookie")
                ).andReturn();

        var sessionCookie = initRequest
                .getResponse()
                .getHeader("Set-Cookie")
                .split(";")[0]
                .split("=")[1];

        testRequest(new SubmitRequest("port"), sessionCookie)
                .andDo(print())
                .andExpectAll(
                        jsonPath("$.gameOver", Matchers.is(false)),
                        jsonPath("$.error").doesNotExist(),
                        jsonPath("$.guesses", Matchers.notNullValue()),
                        jsonPath("$.guesses.after", Matchers.empty()),
                        jsonPath("$.guesses.before", Matchers.containsInRelativeOrder("great", "port")),
                        jsonPath("$.lastSubmissionTimestamp", Matchers.notNullValue())
                ).andReturn();
    }

    @Test
    void whenNoGuessWithActiveSession_thenShouldReturnCurrentState() throws Exception {
        when(dictionary.contains(anyString())).thenReturn(true);
        when(wordSelector.randomWord()).thenReturn("answer");

        var initRequest = testRequest(new SubmitRequest("great"), null)
                .andExpectAll(
                        status().isOk(),
                        header().exists("Set-Cookie")
                ).andReturn();

        var sessionCookie = initRequest
                .getResponse()
                .getHeader("Set-Cookie")
                .split(";")[0]
                .split("=")[1];

        testRequest(null, sessionCookie)
                .andExpectAll(
                        jsonPath("$.gameOver", Matchers.is(false)),
                        jsonPath("$.error").doesNotExist(),
                        jsonPath("$.guesses", Matchers.notNullValue()),
                        jsonPath("$.guesses.after", Matchers.empty()),
                        jsonPath("$.guesses.before", Matchers.containsInRelativeOrder("great")),
                        jsonPath("$.lastSubmissionTimestamp", Matchers.notNullValue())
                );
    }

    @Test
    void whenInitiatingFirstGuess_thenShouldAssignCookie() throws Exception {
        when(dictionary.contains("guess")).thenReturn(true);
        when(wordSelector.randomWord()).thenReturn("answer");

        testRequest(new SubmitRequest("guess"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guesses.before[0]", Matchers.is("guess")))
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();
    }

    @Test
    void whenUserGuessesCorrectly_thenShouldSetGameOver() throws Exception {
        when(dictionary.contains(anyString())).thenReturn(true);
        when(wordSelector.randomWord()).thenReturn("answer");

        // starts the game
        var initRequest = testRequest(null, null)
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();

        var sessionCookie = initRequest
                .getResponse()
                .getHeader("Set-Cookie")
                .split(";")[0]
                .split("=")[1];

        testRequest(new SubmitRequest("answer"), sessionCookie)
                .andExpect(jsonPath("$.gameOver", Matchers.is(true)));
    }

    private ResultActions testRequest(SubmitRequest request, String sessionCookie) throws Exception {
        var postRequest = post(SUBMIT_API);
        if (request != null) {
            postRequest.content("{\"guess\":\"%s\"}".formatted(request.guess()))
                    .contentType(MediaType.APPLICATION_JSON);
        }
        if (sessionCookie != null) {
            postRequest.cookie(new Cookie("SESSION", sessionCookie));
        }
        return mvc.perform(postRequest);
    }

    @TestConfiguration
    static class SessionIT {
        @MockBean
        Dictionary dictionary;

        @MockBean
        WordSelector wordSelector;
    }

    @Nested
    public class Session {
        @Test
        void whenSessionHasNoInvalidGuesses() throws Exception {
            final String ANSWER = "fire";
            when(dictionary.contains(anyString())).thenReturn(true);
            when(wordSelector.randomWord()).thenReturn(ANSWER);
            final String[] GUESSES = {
                    "traffic",
                    "balloon",
                    "kernel",
                    "great",
                    "dwarf",
                    "early",
                    "fate",
                    "fire"
            };

            final String[] AFTER = {
                    "balloon",
                    "dwarf",
                    "early",
                    "fate"
            };
            final String[] BEFORE = {
                    "great",
                    "kernel",
                    "traffic"
            };

            // starts the game
            var initRequest = testRequest(null, null)
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Set-Cookie"))
                    .andReturn();

            var sessionCookie = initRequest
                    .getResponse()
                    .getHeader("Set-Cookie")
                    .split(";")[0]
                    .split("=")[1];

            ResultActions result = null;
            for (String g : GUESSES) {
                result = testRequest(new SubmitRequest(g), sessionCookie);
            }

            result.andExpectAll(
                    jsonPath("$.gameOver", Matchers.is(true)),
                    jsonPath("$.error").doesNotExist(),
                    jsonPath("$.lastSubmissionTimestamp", Matchers.notNullValue()),
                    jsonPath("$.guesses.after", Matchers.contains(AFTER)),
                    jsonPath("$.guesses.before", Matchers.contains(BEFORE))
            );
        }

        @Test
        void whenUserGuessesOutOfOrder_thenShouldStillPreserveBeforeAndAfterGuesses() throws Exception {

            final String ANSWER = "fire";
            when(dictionary.contains(anyString())).thenReturn(true);
            when(wordSelector.randomWord()).thenReturn(ANSWER);
            final String[] GUESSES = {
                    "early",
                    "traffic",
                    "dwarf",
                    "kernel",
                    "fate",
                    "balloon",
                    "great",
                    "fire"
            };

            final String[] AFTER = {
                    "balloon",
                    "dwarf",
                    "early",
                    "fate"
            };
            final String[] BEFORE = {
                    "great",
                    "kernel",
                    "traffic"
            };

            // starts the game
            var initRequest = testRequest(null, null)
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Set-Cookie"))
                    .andReturn();

            var sessionCookie = initRequest
                    .getResponse()
                    .getHeader("Set-Cookie")
                    .split(";")[0]
                    .split("=")[1];

            ResultActions result = null;
            for (String g : GUESSES) {
                result = testRequest(new SubmitRequest(g), sessionCookie);
            }

            result.andExpectAll(
                    jsonPath("$.gameOver", Matchers.is(true)),
                    jsonPath("$.error").doesNotExist(),
                    jsonPath("$.lastSubmissionTimestamp", Matchers.notNullValue()),
                    jsonPath("$.guesses.after", Matchers.contains(AFTER)),
                    jsonPath("$.guesses.before", Matchers.contains(BEFORE))
            );
        }
    }
}
