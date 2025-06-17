//package tech.best;
//
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Testcontainers
//public class SignMessageSimpleTest {
//
//  @Container
//  static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer(
//          DockerImageName.parse("postgres:16.1")
//  );
//
//  @Container
//  static KafkaContainer kafkaContainer = new KafkaContainer(
//          DockerImageName.parse("confluentinc/cp-kafka:7.5.3")
//  );
//
//  @Test
//  void isStartedTest() throws InterruptedException {
//    Thread.sleep(2000);
//    assertThat(postgreSQLContainer.isRunning()).isTrue();
//    assertThat(kafkaContainer.isRunning()).isTrue();
//  }
//
//  @Test
//  void isStartedAnotherTest() throws InterruptedException {
//    Thread.sleep(2000);
//    assertThat(postgreSQLContainer.isRunning()).isTrue();
//    assertThat(kafkaContainer.isRunning()).isTrue();
//  }
//
//}