package best;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import tech.best.controller.PlainMessage;
import tech.best.controller.SignedMessageDTO;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@MicronautTest
//@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
//@Property(name = "datasources.default.url", value = "jdbc:tc:postgresql:16.1:///tech")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignMessageMicronautTest implements TestPropertyProvider {

    @Inject
    @Client("/")
    HttpClient client;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1");
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka:4.0.0"));

    static {
        kafkaContainer.start();
        postgres.start();
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        return Map.of(
                "datasources.default.url", postgres.getJdbcUrl(),
                "datasources.default.username", postgres.getUsername(),
                "datasources.default.password", postgres.getPassword(),
                "kafka.bootstrap.servers", kafkaContainer.getBootstrapServers()
        );
    }

    @DisplayName("when requesting to sign a text then it is signed")
    @Test
    void signText() {
        var request = HttpRequest.POST("/messages", new PlainMessage("Hello"));
        var response = client.toBlocking().exchange(request);

        assertThat(response.getStatus().getCode()).isEqualTo(202);

        var location = response.getHeaders().get("Location");
        String messageId = location.replace("/messages/", "");

        await().untilAsserted(() -> {
            var req = HttpRequest.GET("/messages/" + messageId);
            var message = client.toBlocking().exchange(req, SignedMessageDTO.class).body();
            assertThat(message.signature()).isNotNull();
        });
    }
}