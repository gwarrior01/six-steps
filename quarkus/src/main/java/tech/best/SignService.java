package tech.best;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class SignService {

  private static final Logger LOG = LoggerFactory.getLogger(SignService.class);

  @Inject
  @Channel("signed-messages")
  Emitter<SignedMessage> emitter;

  @Incoming("unsigned-messages")
  public void handleMessage(UnsignedMessage messageToSign) {
    // Warning! Never use this in production. Use a library and use a proper and modern hashing algorithm
    String signature = "AAAAA";

    LOG.debug("Message signed. [messageId={}, text={}]", messageToSign.messageId(), messageToSign.text());

    emitter.send(new SignedMessage(messageToSign.messageId(), signature));
  }

  public record UnsignedMessage(UUID messageId, String text) {

  }

  public record SignedMessage(UUID messageId, String signature) {

  }

}