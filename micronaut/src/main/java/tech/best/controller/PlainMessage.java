package tech.best.controller;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record PlainMessage(String text) {

}