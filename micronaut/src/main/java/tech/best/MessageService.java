package tech.best;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.best.kafka.UnsingedMessageProducer;
import tech.best.repository.MessageEntity;
import tech.best.repository.MessageRepository;

import java.time.Instant;
import java.util.UUID;

@Singleton
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final UnsingedMessageProducer producer;

    public MessageService(MessageRepository messageRepository, UnsingedMessageProducer producer) {
        this.messageRepository = messageRepository;
        this.producer = producer;
    }

    public UUID save(String text) {
        var messageEntity = messageRepository.save(new MessageEntity(UUID.randomUUID(), text));
        LOG.info("Requesting to sign a message. [messageId={}, text={}]", messageEntity.getId(), messageEntity.getText());
        producer.send(new SignService.UnsignedMessage(messageEntity.getId(), text));
        return messageEntity.getId();
    }

    @KafkaListener(
            clientId = "signed-message",
            groupId = "messages"
    )
    @Topic("messages.signed")
    public void handleMessage(SignService.SignedMessage signedMessage) {
        messageRepository
                .findById(signedMessage.messageId())
                .ifPresent(messageEntity -> {
                    messageEntity.setSignature(signedMessage.signature());
                    messageEntity.setSignedAt(Instant.now());
                    messageRepository.update(messageEntity);
                });
    }

}