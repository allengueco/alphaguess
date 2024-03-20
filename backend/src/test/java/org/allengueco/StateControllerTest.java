package org.allengueco;

import jakarta.servlet.http.Cookie;
import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.Dictionary;
import org.allengueco.game.WordSelector;
import org.allengueco.repository.GameRepository;
import org.allengueco.rest.StateController;
import org.eclipse.collections.api.factory.Maps;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = StateController.class)
class StateControllerTest {
    private final Logger log = LoggerFactory.getLogger(StateControllerTest.class);
    @MockBean
    Dictionary dictionary;
    @MockBean
    WordSelector wordSelector;
    @Autowired
    MockMvc mvc;


    @Test
    void submitTwiceValid() throws Exception {
        when(wordSelector.randomWord()).thenReturn("case");
        when(dictionary.contains(anyString())).thenReturn(true);

        var first = testRequest(new SubmitRequest("one"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guesses.before", Matchers.contains("one")))
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();
        var firstCookie = first.getResponse().getHeader("Set-Cookie").split(";")[0].split("=")[1];

        testRequest(new SubmitRequest("two"), firstCookie)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guesses.before", containsInRelativeOrder("one", "two")))
                .andReturn();

        testRequest(new SubmitRequest("tyrant"), firstCookie)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guesses.before", containsInRelativeOrder("one", "two", "tyrant")))
                .andReturn();
    }

    @Test
    void submitOnceValid() throws Exception {
        when(wordSelector.randomWord()).thenReturn("case");
        when(dictionary.contains(anyString())).thenReturn(true);

        testRequest(new SubmitRequest("apple"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guesses.after", Matchers.contains("apple")))
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();
    }

    private ResultActions testRequest(SubmitRequest request, String sessionCookie) throws Exception {
        var postRequest = post("/api/submit")
                .content("""
                        {"guess" : "%s"}
                        """.formatted(request.guess()))
                .contentType(MediaType.APPLICATION_JSON);
        if (sessionCookie != null) {
            postRequest.cookie(new Cookie("SESSION", sessionCookie));
        }
        return mvc.perform(postRequest);
    }

    @TestConfiguration
    @EnableSpringHttpSession
    static class StateControllerConfiguration {
        @MockBean
        GameRepository gameRepository;

        @Bean
        MapSessionRepository sessionRepository() {
            return new MapSessionRepository(Maps.mutable.empty());
        }
    }
}