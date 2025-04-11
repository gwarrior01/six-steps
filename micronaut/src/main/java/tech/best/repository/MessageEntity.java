package tech.best.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "message")
public class MessageEntity {

  @Id
  private UUID id;

  private String text;

  @Column(name = "created_at")
  private Instant createdAt = Instant.now();

  private String signature;

  @Column(name = "signed_at")
  private Instant signedAt;

  public MessageEntity() {
  }

  public MessageEntity(UUID id, String text) {
    this.id = id;
    this.text = text;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature( String signature) {
    this.signature = signature;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getSignedAt() {
    return signedAt;
  }

  public void setSignedAt(Instant signedAt) {
    this.signedAt = signedAt;
  }

}