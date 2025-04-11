package tech.best.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, UUID> {
}