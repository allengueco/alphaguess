package org.allengueco.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.allengueco.game.states.GameSession;
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
    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    RedisSerializer<GameSession> redisSerializer(ObjectMapper mapper) {
        return new Jackson2JsonRedisSerializer<>(mapper, GameSession.class);
    }

    @Bean
    RedisTemplate<?, ?> sessionRedisTemplate(
            RedisConnectionFactory redisConnectionFactory,
            RedisSerializer<GameSession> gameSessionRedisSerializer) {
        final RedisTemplate<String, GameSession> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        redisTemplate.setDefaultSerializer(gameSessionRedisSerializer);

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
