package tech.best.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import tech.best.MessageService;
import tech.best.repository.MessageEntity;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class SignMessageController {

  @Inject
  MessageService messageService;

  @POST
  public Response signMessage(PlainMessage plainMessage) {
    UUID messageId = messageService.save(plainMessage.text());
    URI location = UriBuilder.fromPath("/messages/{id}").build(messageId);
    return Response.accepted().location(location).build();
  }

  @GET
  @Path("/{messageId}")
  public Response findSignedMessage(@PathParam("messageId") UUID messageId) {
    MessageEntity message = MessageEntity.findById(messageId);
    if (message == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(toDto(message)).build();
  }

  @GET
  public List<SignedMessageDTO> findAll() {
    return MessageEntity.<MessageEntity>listAll()
            .stream()
            .map(this::toDto)
            .toList();
  }

  private SignedMessageDTO toDto(MessageEntity message) {
    return new SignedMessageDTO(message.id, message.text, message.signature, message.signedAt);
  }

}