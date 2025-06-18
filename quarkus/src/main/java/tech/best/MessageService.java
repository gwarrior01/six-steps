package tech.best;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import tech.best.repository.MessageEntity;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.best.repository.MessageRepo;

@ApplicationScoped
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Channel("unsigned-messages")
    Emitter<SignService.UnsignedMessage> emitter;
    @Inject
    MessageRepo messageRepo;

    public UUID save(String text) {
        MessageEntity messageEntity = new MessageEntity(UUID.randomUUID(), text);
        messageRepo.save(messageEntity);
        LOG.info("Requesting to sign a message. [messageId={}, text={}]", messageEntity.id, messageEntity.text);
        emitter.send(new SignService.UnsignedMessage(messageEntity.id, messageEntity.text));
        return messageEntity.id;
    }

    @Incoming("signed-messages")
    @Blocking
    @Transactional
    public void handleMessage(SignService.SignedMessage signedMessage) {
        MessageEntity messageEntity = MessageEntity.findById(signedMessage.messageId());
        if (messageEntity != null) {
            LOG.info("Add sign to the message. [messageId={}, text={}]", messageEntity.id, messageEntity.text);
            messageEntity.signature = signedMessage.signature();
            messageEntity.signedAt = Instant.now();
            messageEntity.persist();
        }
    }

}