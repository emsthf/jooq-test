package com.sight.jooqfirstlook;

import com.sight.jooqfirstlook.actor.ActorFilmography;
import com.sight.jooqfirstlook.actor.ActorFilmographySearchOption;
import com.sight.jooqfirstlook.actor.ActorRepository;
import org.jooq.generated.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqConditionTest {

    @Autowired
    ActorRepository actorRepository;

    @DisplayName("and 조건 검색 - firstName과 LastName이 일치하는 배우 조회")
    @Test
    void AND조건_1() {
        // given
        String fistName = "ED";
        String lastName = "CHASE";

        // when
        List<Actor> actors = actorRepository.findByFirstNameAndLastName(fistName, lastName);

        // then
        assertThat(actors).hasSize(1);
    }

    @DisplayName("or 조건 검색 - firstName 또는 LastName이 일치하는 배우 조회")
    @Test
    void OR조건_1() {
        // given
        String fistName = "ED";
        String lastName = "CHASE";

        // when
        List<Actor> actors = actorRepository.findByFirstNameOrLastName(fistName, lastName);

        // then
        assertThat(actors).hasSizeGreaterThan(1);
    }

    @DisplayName("in절 - 동적 조건 검색")
    @Test
    void IN절_동적_조건검색_1() {
        // given

        // when
        List<Actor> actors = actorRepository.findByActorIdIn(List.of(1L));

        // then
        assertThat(actors).hasSize(1);
    }

    @DisplayName("in절 - 동적 조건 검색 - empty list 시 제외")
    @Test
    void IN절_동적_조건검색_2() {
        // given

        // when
        List<Actor> actors = actorRepository.findByActorIdIn(Collections.emptyList());

        // then
        assertThat(actors).hasSizeGreaterThan(1);
    }
    
    @DisplayName("다중 조건 검색 - 배우 이름으로 조회")
    @Test
    void 다중_조건_검색_1() {
        // given
        var searchOption = ActorFilmographySearchOption.builder()
                .actorName("LOLLOBRIGIDA")
                .build();
        
        // when
        List<ActorFilmography> actorFilmographies = actorRepository.findActorFilmography(searchOption);
        
        // then
        assertThat(actorFilmographies).hasSize(1);
    }

    @DisplayName("다중 조건 검색 - 배우 이름과 영화 제목으로 조회")
    @Test
    void 다중_조건_검색_2() {
        // given
        var searchOption = ActorFilmographySearchOption.builder()
                .actorName("LOLLOBRIGIDA")
                .filmTitle("COMMANDMENTS EXPRESS")
                .build();

        // when
        List<ActorFilmography> actorFilmographies = actorRepository.findActorFilmography(searchOption);

        // then
        assertThat(actorFilmographies).hasSize(1);
        assertThat(actorFilmographies.get(0).getFilmList()).hasSize(1);
    }
}
