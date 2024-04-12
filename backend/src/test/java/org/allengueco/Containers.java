package org.allengueco;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface Containers {
    @Container
    @ServiceConnection
    PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
            .withDatabaseName("betaguess");
}
