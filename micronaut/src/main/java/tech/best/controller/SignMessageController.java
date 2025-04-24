package tech.best.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import tech.best.MessageService;
import tech.best.repository.MessageEntity;
import tech.best.repository.MessageRepository;
import tech.best.repository.MessageRepositoryJooq;
import tech.best.tables.pojos.Message;

import java.net.URI;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Controller("/messages")
class SignMessageController {

    private final MessageService messageService;
    private final MessageRepositoryJooq messageRepository;

    public SignMessageController(MessageService messageService, MessageRepositoryJooq messageRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    @Post
    public HttpResponse<Object> signMessage(@Body PlainMessage plainMessage) throws Exception{
        UUID messageId = messageService.save(plainMessage.text());

        var uri = new URI("/messages/" + messageId);
        return HttpResponse
                .accepted()
                .headers(headers -> headers.location(uri));
    }

    @Get("/{messageId}")
    public HttpResponse<SignedMessageDTO> findSignedMessage(@PathVariable UUID messageId) {
        return messageRepository
                .findById(messageId)
                .map(message -> HttpResponse.ok(toDtoFromJooq(message)))
                .orElse(HttpResponse.notFound());
    }

    @Get
    public List<SignedMessageDTO> findAll() {
        return messageRepository
                .findAll()
                .stream()
                .map(this::toDtoFromJooq)
                .toList();
    }

//    private SignedMessageDTO toDto(MessageEntity message) {
//        return new SignedMessageDTO(message.getId(), message.getText(), message.getSignature(), message.getSignedAt());
//    }

    private SignedMessageDTO toDtoFromJooq(Message message) {
        var signedAt = message.getSignedAt() != null ? message.getSignedAt().toInstant(ZoneOffset.UTC) : null;
        return new SignedMessageDTO(message.getId(), message.getText(), message.getSignature(), signedAt);
    }

}