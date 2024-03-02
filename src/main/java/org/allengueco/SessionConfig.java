package org.allengueco;

import org.springframework.boot.web.server.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSpringWebSession
public class SessionConfig {
    private final Duration SESSION_TIMEOUT = Duration.ofHours(1);

    @Bean
    ReactiveMapSessionRepository sessionRepository() {
        var sessionRepo = new ReactiveMapSessionRepository(new ConcurrentHashMap<>());

        sessionRepo.setDefaultMaxInactiveInterval(SESSION_TIMEOUT);

        return sessionRepo;
    }

    @Bean
    WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("BGSESSION");
        resolver.addCookieInitializer((builder) -> builder
                .path("/")
                .sameSite(Cookie.SameSite.STRICT.attributeValue()));
        return resolver;
    }
}
