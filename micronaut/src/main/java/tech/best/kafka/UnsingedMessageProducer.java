package tech.best.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import tech.best.SignService;

@KafkaClient
public interface UnsingedMessageProducer {

  @Topic("messages.unsigned")
  void send(SignService.UnsignedMessage message);

}