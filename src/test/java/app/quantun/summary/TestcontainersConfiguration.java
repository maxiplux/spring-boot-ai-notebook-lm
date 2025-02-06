package app.quantun.summary;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("apache/kafka-native:3.9.0"));
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> pgvectorContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg16"));
    }


}
