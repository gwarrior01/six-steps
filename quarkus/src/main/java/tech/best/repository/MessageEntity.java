package tech.best.repository;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@RegisterForReflection
@Table(name = "message")
public class MessageEntity extends PanacheEntityBase {

  @Id
  public UUID id;

  public String text;

  @Column(name = "created_at")
  public Instant createdAt = Instant.now();

  public String signature;

  @Column(name = "signed_at")
  public Instant signedAt;

  public MessageEntity() {
  }

  public MessageEntity(UUID id, String text) {
    this.id = id;
    this.text = text;
  }
}