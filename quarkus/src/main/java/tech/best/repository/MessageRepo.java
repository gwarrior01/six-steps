package tech.best.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MessageRepo {

    @Transactional
    public void save(MessageEntity messageEntity) {
        messageEntity.persist();
    }
}