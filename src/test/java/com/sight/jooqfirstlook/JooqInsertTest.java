package com.sight.jooqfirstlook;

import com.sight.jooqfirstlook.actor.ActorRepository;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqInsertTest {

    @Autowired
    ActorRepository actorRepository;

    @DisplayName("자동 생성된 DAO를 통한 insert")
    @Transactional
    @Test
    void insert_dao() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        actorRepository.saveByDao(actor);

        // then
        assertThat(actor.getActorId()).isNotNull();
    }

    @DisplayName("ActiveRecord를 통한 insert")
    @Transactional
    @Test
    void insert_by_record() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        ActorRecord actorRecord = actorRepository.saveByRecord(actor);

        // then
        assertThat(actorRecord.getActorId()).isNotNull();
        assertThat(actor.getActorId()).isNull();
    }

    @DisplayName("SQL 실행 후 PK만 반환")
    @Transactional
    @Test
    void insert_with_returning_pk() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Long pk = actorRepository.saveWithReturningPkOnly(actor);

        // then
        assertThat(pk).isNotNull();
    }

    @DisplayName("SQL 실행 후 해당 ROW 반환")
    @Transactional
    @Test
    void insert_with_returning() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");

        // when
        Actor newActor = actorRepository .saveWithReturning(actor);

        // then
        assertThat(newActor).hasNoNullFieldsOrProperties();
    }

    /**
     * insert into `actor` (`first_name`, `last_name`, `last_update`) values (?, ?), (?, ?), (?, ?)
     */
    @DisplayName("bulk insert 예제")
    @Transactional
    @Test
    void bulk_insert() {
        // given
        Actor actor1 = new Actor();
        actor1.setFirstName("John");
        actor1.setLastName("Doe");
        actor1.setLastUpdate(LocalDateTime.now());

        Actor actor2 = new Actor();
        actor2.setFirstName("John2");
        actor2.setLastName("Doe2");
        actor2.setLastUpdate(LocalDateTime.now());

        List<Actor> actorList = List.of(actor1, actor2);

        // when
        actorRepository.bulkInsertWithRows(actorList);

        // then

    }
}
