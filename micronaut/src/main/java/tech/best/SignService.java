package tech.best;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.serde.annotation.Serdeable;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.best.kafka.SingedMessageProducer;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@KafkaListener(
        clientId = "unsigned-message",
        groupId = "messages"
)
public class SignService {

    private static final Logger LOG = LoggerFactory.getLogger(SignService.class);

    private final SingedMessageProducer producer;

    public SignService(SingedMessageProducer producer) throws NoSuchAlgorithmException {
        this.producer = producer;
    }

    @Topic("messages.unsigned")
    public void receive(UnsignedMessage messageToSign) {
        String signature = DigestUtils.md5Hex(messageToSign.text()).toUpperCase();
        LOG.debug("Message signed. [messageId={}, text={}]", messageToSign.messageId(), messageToSign.text());
        producer.send(new SignedMessage(messageToSign.messageId(), signature));
    }

    @Serdeable
    public record UnsignedMessage(UUID messageId, String text) {

    }

    @Serdeable
    public record SignedMessage(UUID messageId, String signature) {

    }

}