package tech.best;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.best.kafka.UnsingedMessageProducer;
import tech.best.repository.MessageRepositoryJooq;
import tech.best.tables.pojos.Message;

import java.time.LocalDateTime;
import java.util.UUID;

@Singleton
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepositoryJooq messageRepository;
    private final UnsingedMessageProducer producer;

    public MessageService(MessageRepositoryJooq messageRepository, UnsingedMessageProducer producer) {
        this.messageRepository = messageRepository;
        this.producer = producer;
    }

    public UUID save(String text) {
        var message = new Message(UUID.randomUUID(), text, LocalDateTime.now(), null, null);
        messageRepository.save(message);
        LOG.debug("Requesting to sign a message. [messageId={}, text={}]", message.getId(), message.getText());
        producer.send(new SignService.UnsignedMessage(message.getId(), text));
        return message.getId();
    }

    @KafkaListener(
            clientId = "signed-message",
            groupId = "messages"
    )
    @Topic("messages.signed")
    public void handleMessage(SignService.SignedMessage signedMessage) {
        messageRepository
                .findById(signedMessage.messageId())
                .ifPresent(message ->
                    messageRepository.save(
                            new Message(
                                    message.getId(),
                                    message.getText(),
                                    message.getCreatedAt(),
                                    signedMessage.signature(), LocalDateTime.now())
                    )
                );
    }

}