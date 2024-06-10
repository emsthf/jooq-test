package com.sight.jooqfirstlook;

import com.sight.jooqfirstlook.actor.ActorRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqActiveRecordTest {

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    DSLContext dslContext;

    @DisplayName("SELECT 절 예제")
    @Test
    void activeRecord() {
        // given
        Long actorId = 1L;

        // when
        ActorRecord actorRecord = actorRepository.findRecordByActorId(actorId);

        // then
        assertThat(actorRecord).hasNoNullFieldsOrProperties();
    }

    @DisplayName("activeRecord refresh 예제")
    @Test
    void activeRecord_refresh() {
        // given
        Long actorId = 1L;
        ActorRecord actorRecord = actorRepository.findRecordByActorId(actorId);
        actorRecord.setFirstName(null);

        // when
        actorRecord.refresh(JActor.ACTOR.FIRST_NAME);

        // then
        assertThat(actorRecord.getFirstName()).isNotBlank();
    }

    @DisplayName("activeRecord store 예제 - insert")
    @Transactional
    @Test
    void activeRecord_insert_예제() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        // when
        actorRecord.setFirstName("John");
        actorRecord.setLastName("Doe");
        actorRecord.store();  // actorRecord.insert()도 가능
        actorRecord.refresh();

        // then
        assertThat(actorRecord.getLastUpdate()).isNotNull();
    }

    @DisplayName("activeRecord store 예제 - update")
    @Transactional
    @Test
    void activeRecord_update_예제() {
        // given
        Long actorId = 1L;
        String newName = "test";
        ActorRecord actorRecord = actorRepository.findRecordByActorId(actorId);

        // when
        actorRecord.setFirstName(newName);
        actorRecord.store();  // 혹은 actorRecord.update()도 가능

        // then
        assertThat(actorRecord.getFirstName()).isEqualTo(newName);
    }

    @DisplayName("activeRecord delete 예제")
    @Transactional
    @Test
    void activeRecord_delete_예제() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        actorRecord.setFirstName("John");
        actorRecord.setLastName("Doe");
        actorRecord.store();

        // when
        int result = actorRecord.delete();

        // then
        assertThat(result).isEqualTo(1);
    }
}
