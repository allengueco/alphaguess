package org.allengueco;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class ContainersConfig {
    @Bean
    @ServiceConnection(name = "redis")
    RedisContainer redisContainer() {
        return new RedisContainer(DockerImageName.parse("redis:latest"));
    }
}
