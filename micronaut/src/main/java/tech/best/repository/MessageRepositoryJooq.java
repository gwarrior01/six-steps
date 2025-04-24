package tech.best.repository;

import io.micronaut.data.annotation.Repository;
import jakarta.transaction.Transactional;
import org.jooq.DSLContext;
import tech.best.tables.pojos.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static tech.best.Tables.MESSAGE;

@Repository
public class MessageRepositoryJooq  {

    private final DSLContext dsl;

    public MessageRepositoryJooq(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional
    public Optional<Message> findById(UUID id) {
        return dsl.selectFrom(MESSAGE)
                .where(MESSAGE.ID.eq(id))
                .fetchOptionalInto(Message.class);
    }

    @Transactional
    public List<Message> findAll() {
        return dsl.selectFrom(MESSAGE)
                .fetchInto(Message.class);
    }

    @Transactional
    public void save(Message message) {
        // upsert (insert on conflict do update)
        dsl.insertInto(MESSAGE)
                .set(MESSAGE.ID, message.getId())
                .set(MESSAGE.TEXT, message.getText())
                .set(MESSAGE.CREATED_AT, message.getCreatedAt())
                .set(MESSAGE.SIGNATURE, message.getSignature())
                .set(MESSAGE.SIGNED_AT, message.getSignedAt())
                .onConflict(MESSAGE.ID)
                .doUpdate()
                .set(MESSAGE.TEXT, message.getText())
                .set(MESSAGE.SIGNATURE, message.getSignature())
                .set(MESSAGE.SIGNED_AT, message.getSignedAt())
                .execute();
    }
}