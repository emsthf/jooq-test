package com.sight.jooqfirstlook;

import com.sight.jooqfirstlook.actor.ActorRepository;
import com.sight.jooqfirstlook.actor.ActorUpdateRequest;
import org.jooq.generated.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqUpdateTest {

    @Autowired
    ActorRepository actorRepository;

    @DisplayName("pojo를 사용한 update")
    @Transactional
    @Test
    void 업데이트_with_pojo() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Actor actor = actorRepository.saveWithReturning(newActor);

        // when
        actor.setFirstName("Suri");
        actorRepository.update(actor);

        // then
        Actor updatedActor = actorRepository.findByActorId(actor.getActorId());
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");
    }

    @DisplayName("일부 필드만 update - DTO 활용")
    @Transactional
    @Test
    void 업데이트_일부_필드만() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);
        var request = ActorUpdateRequest.builder()
                .firstName("Suri")
                .build();

        // when
        actorRepository.updateWithDto(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");
    }

    @DisplayName("일부 필드만 update - record 활용")
    @Transactional
    @Test
    void 업데이트_일부_필드만_with_record() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);
        var request = ActorUpdateRequest.builder()
                .firstName("Suri")
                .build();

        // when
        actorRepository.updateWithRecord(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");
    }

    @DisplayName("delete 예제")
    @Transactional
    @Test
    void delete_예제() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);

        // when
        int result = actorRepository.delete(newActorId);

        // then
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("delete 예제 - with active record")
    @Transactional
    @Test
    void delete_with_active_record_예제() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);

        // when
        int result = actorRepository.deleteWithRecord(newActorId);

        // then
        assertThat(result).isEqualTo(1);

    }
}
