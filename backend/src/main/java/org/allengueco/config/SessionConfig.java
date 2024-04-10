package org.allengueco.config;

import com.fasterxml.jackson.datatype.eclipsecollections.EclipseCollectionsModule;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableSpringHttpSession
public class SessionConfig {
    private final Logger log = LoggerFactory.getLogger(SessionConfig.class);

    @Bean
    EclipseCollectionsModule eclipseCollectionsModule() {
        return new EclipseCollectionsModule();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookiePath("/api/submit");
        serializer.setCookieMaxAge(3600);
        return serializer;
    }

    @Bean
    MapSessionRepository sessionRepository() {
        return new MapSessionRepository(ConcurrentHashMap.newMap());
    }
}
