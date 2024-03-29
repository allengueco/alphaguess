package org.allengueco.config;

import org.allengueco.commands.Submit;
import org.allengueco.service.BetaGuessClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.shell.command.annotation.EnableCommand;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.net.http.HttpClient;

@EnableCommand(Submit.class)
@ConditionalOnProperty(value = "cli")
@Configuration(proxyBeanMethods = false)
public class CliConfig {
    private static final Logger log = LoggerFactory.getLogger(CliConfig.class);

    @Bean
    BetaGuessClient betaGuessClient(RestClient restClient) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(BetaGuessClient.class);
    }

    @Bean
    RestClient restClient(ClientHttpRequestInterceptor interceptor) {
        return RestClient.builder()
                .baseUrl("http://localhost:8080")
                .requestFactory(new JdkClientHttpRequestFactory(HttpClient.newBuilder().build()))
                .requestInterceptor(interceptor)
                .build();
    }

    @Bean
    ClientHttpRequestInterceptor interceptor() {
        return new StatefulInterceptor();
    }

    private static class StatefulInterceptor implements ClientHttpRequestInterceptor {
        private String cookie;

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            if (this.cookie != null) {
                request.getHeaders().add(HttpHeaders.COOKIE, this.cookie);
            }
            var response = execution.execute(request, body);

            if (this.cookie == null) {
                cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            }

            return response;
        }
    }
}
