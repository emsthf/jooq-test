package com.sight.jooqfirstlook.film;

import com.sight.jooqfirstlook.web.FilmWithActorPagedResponse;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FilmRepositoryTest {

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    FilmService filmService;

    @DisplayName("1) 영화정보 조")
    @Test
    void selectFilmOne() {
        // given
        
        // when
        Film film = filmRepository.findById(1L);
        
        // then
        assertThat(film).isNotNull();
    }

    @DisplayName("2) 영화정보 간략 조회")
    @Test
    void selectFilmInfoOne() {
        // given

        // when
        SimpleFilmInfo simpleFilmInfo = filmRepository.findSimpleFilmInfoById(1L);

        // then
        assertThat(simpleFilmInfo).hasNoNullFieldsOrProperties();
    }

    @DisplayName("3) 영화와 영화에 출현한 배우 정보를 페이징하여 조회")
    @Test
    void selectFilmAndActorWithPaging() {
        // given

        // when
        FilmWithActorPagedResponse filmActorPageResponse = filmService.getFilmActorPageResponse(1L, 20L);

        // then
        assertThat(filmActorPageResponse.getFilmActorList())
                .hasSize(20);
    }
}