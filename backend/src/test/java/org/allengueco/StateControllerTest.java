package org.allengueco;

import org.allengueco.dto.ActionResult;
import org.allengueco.dto.SubmitRequest;
import org.allengueco.game.Dictionary;
import org.allengueco.game.WordSelector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = StateController.class)
@Import(StateControllerTest.StateControllerContext.class)
class StateControllerTest {
    @Configuration
    static class StateControllerContext {
        @Bean
        GameService gameService() {
            return new GameService();
        }
    }

    @Autowired
    GameService gameService;

    @MockBean
    Dictionary dictionary;

    @MockBean
    WordSelector wordSelector;

    @Autowired
    WebTestClient client;

    @Test
    void submitTwiceValid() {
        when(wordSelector.randomWord()).thenReturn("case");
        when(dictionary.contains("one")).thenReturn(true);
        when(dictionary.contains("two")).thenReturn(true);


        var first = testRequest(new SubmitRequest("one"))
//                .expectCookie().exists("BGSESSION")
                .expectStatus().isOk()
                .expectBody(ActionResult.class)
                .returnResult();

        var second = testRequest(new SubmitRequest("two"))
                .expectCookie().exists("BGSESSION")
                .expectStatus().isOk()
                .expectBody(ActionResult.class)
                .returnResult();
        var sessionCookieValue = second.getResponseCookies().getFirst("BGSESSION");

        assertNotNull(sessionCookieValue);
    }

    private WebTestClient.ResponseSpec testRequest(SubmitRequest request) {
        return client.post()
                .uri("/api/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), SubmitRequest.class)
                .exchange();
    }
}