package tech.best.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.UUID;

@Serdeable
public record SignedMessageDTO(
    @JsonProperty("message_id")
    UUID messageId,

    @JsonProperty("message")
    String message,

    @JsonProperty("signature")
    String signature,

    @JsonProperty("signed_at")
    Instant signedAt
) {
}