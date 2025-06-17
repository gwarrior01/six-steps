package tech.best.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record SignedMessageDTO(
        @JsonProperty("message_id")
        UUID messageId,

        @JsonProperty("message")
        String message,

        @JsonProperty("signature")
        String signature,

        @JsonProperty("signed_at")
        Instant signedAt) {
}