package tech.best;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Service
public class SignService {

  private static final Logger LOG = LoggerFactory.getLogger(SignService.class);

  private final KafkaTemplate<String, SignedMessage> kafkaTemplate;

  public SignService(KafkaTemplate<String, SignedMessage> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(id = "sign-message", groupId = "messages", topics = "messages.unsigned")
  public void handleMessage(@Payload UnsignedMessage messageToSign) {
    // Warning! Never use this in production. Use a library and use a proper and modern hashing algorithm
    String signature = DigestUtils.md5DigestAsHex(messageToSign.text().getBytes());

    LOG.debug("Message signed. [messageId={}, text={}]", messageToSign.messageId(), messageToSign.text());

    kafkaTemplate.send("messages.signed", new SignedMessage(messageToSign.messageId(), signature));
  }

  public record UnsignedMessage(UUID messageId, String text) {

  }

  public record SignedMessage(UUID messageId, String signature) {

  }

}

