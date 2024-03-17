package org.allengueco.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.eclipsecollections.EclipseCollectionsModule;
import org.allengueco.game.states.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisIndexedHttpSession
public class SessionConfig {
    private final Logger log = LoggerFactory.getLogger(SessionConfig.class);
    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    EclipseCollectionsModule eclipseCollectionsModule() {
        return new EclipseCollectionsModule();
    }

    @Bean
    RedisSerializer<GameSession> redisSerializer(ObjectMapper mapper) {
        return new Jackson2JsonRedisSerializer<>(mapper, GameSession.class);
    }

    @Bean
    RedisTemplate<String, GameSession> redisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            RedisSerializer<GameSession> gameSessionRedisSerializer) {
        final RedisTemplate<String, GameSession> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(gameSessionRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookiePath("/api");
        serializer.setCookieMaxAge(3600);
        return serializer;
    }

}
