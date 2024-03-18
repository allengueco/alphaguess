package org.allengueco;

import com.redis.testcontainers.RedisContainer;
import jakarta.servlet.http.Cookie;
import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.Dictionary;
import org.allengueco.game.WordSelector;
import org.allengueco.repository.GameRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class GameSessionIT {
    @Container
    @ServiceConnection
    static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest"));
    private static final Logger log = LoggerFactory.getLogger(GameSessionIT.class);
    final String SUBMIT_API = "/api/submit";
    @Autowired
    MockMvc mvc;
    @Autowired
    Dictionary dictionary;
    @Autowired
    WordSelector wordSelector;
    @Autowired
    GameRepository gameRepository;

    @Test
    void guessOnceWithoutExistingCookie() throws Exception {
        when(dictionary.contains("guess")).thenReturn(true);
        when(wordSelector.randomWord()).thenReturn("answer");
        var response = testRequest(new SubmitRequest("guess"), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guesses.before[0]", Matchers.is("guess")))
                .andExpect(header().exists("Set-Cookie"))
                .andReturn();

        var cookie = response.getResponse().getHeader("Set-Cookie").split(";")[0].split("=")[1];


        gameRepository.findAll().forEach(s -> log.info(s.toString()));
    }

    private ResultActions testRequest(SubmitRequest request, String sessionCookie) throws Exception {
        var postRequest = post(SUBMIT_API)
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
    static class SessionIT {
        @MockBean
        Dictionary dictionary;

        @MockBean
        WordSelector wordSelector;
    }

}
