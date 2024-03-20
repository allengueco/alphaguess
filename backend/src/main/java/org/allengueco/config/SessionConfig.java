package org.allengueco.config;

import com.fasterxml.jackson.datatype.eclipsecollections.EclipseCollectionsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession
public class SessionConfig {
    private final Logger log = LoggerFactory.getLogger(SessionConfig.class);

    @Bean
    EclipseCollectionsModule eclipseCollectionsModule() {
        return new EclipseCollectionsModule();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookiePath("/api");
        serializer.setCookieMaxAge(3600);
        return serializer;
    }

    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        var template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        return template;
    }

//    @Bean
//    Jackson2JsonRedisSerializer<Guesses> guessesSerializer(ObjectMapper mapper) {
//        return new Jackson2JsonRedisSerializer<>(mapper, TypeFactory.defaultInstance().constructType(Guesses.class),
//                (m, source, type) -> m.readValue(source, 0, source.length, type),
//                JacksonObjectWriter.create());
//    }

//    @Bean
//    RedisCustomConversions redisCustomConversions(
//            GuessesToBytesConverter guessesToBytesConverter,
//            BytesToGuessesConverter bytesToGuessesConverter
//    ) {
//        return new RedisCustomConversions(List.of(guessesToBytesConverter, bytesToGuessesConverter));
//    }
}
