package tech.best;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
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
    String signature = md5Hex(messageToSign.text());

    LOG.info("Message signed. [messageId={}, text={}]", messageToSign.messageId(), messageToSign.text());

    emitter.send(new SignedMessage(messageToSign.messageId(), signature));
  }

  private String md5Hex(String text) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public record UnsignedMessage(UUID messageId, String text) {

  }

  public record SignedMessage(UUID messageId, String signature) {

  }

}
