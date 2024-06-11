package com.sight.jooqfirstlook;

import com.sight.jooqfirstlook.film.FilmRepository;
import com.sight.jooqfirstlook.film.FilmWithActor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqJoinShotCutTest {

    @Autowired
    FilmRepository filmRepository;

    @DisplayName("implicitPathJoin_테스트")
    @Test
    void implicitPathJoin_테스트() {

        List<FilmWithActor> original = filmRepository.findFilmWithActorList(1L, 10L);
        List<FilmWithActor> implicit = filmRepository.findFilmWithActorListImplicitPathJoin(1L, 10L);

        assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }

    @Test
    @DisplayName("explicitPathJoin_테스트")
    void explicitPathJoin_테스트() {

        List<FilmWithActor> original = filmRepository.findFilmWithActorList(1L, 10L);
        List<FilmWithActor> implicit = filmRepository.findFilmWithActorListExplicitPathJoin(1L, 10L);

        assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }
}
