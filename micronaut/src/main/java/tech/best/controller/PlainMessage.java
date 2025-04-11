package tech.best.controller;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable.Deserializable
public record PlainMessage(String text) {

}