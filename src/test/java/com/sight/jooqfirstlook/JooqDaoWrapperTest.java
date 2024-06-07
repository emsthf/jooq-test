package com.sight.jooqfirstlook;

import com.sight.jooqfirstlook.film.FilmRepositoryHasA;
import com.sight.jooqfirstlook.film.FilmRepositoryIsA;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqDaoWrapperTest {

    @Autowired
    FilmRepositoryIsA filmRepositoryIsA;  // 상속

    @Autowired
    FilmRepositoryHasA filmRepositoryHasA;  // 컴포지트

    @DisplayName("""
            상속) 자동생성 DAO 사용 - 영화 길이가 100 ~ 180분 사이인 영화 조회
            """)
    @Test
    void 상속_dao_1() {
        // given
        var start = 100;
        var end = 180;

        // when
        List<Film> films = filmRepositoryIsA.fetchRangeOfJLength(start, end);

        // then
        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }

    @DisplayName("""
            컴포지션) DAO Wrapper 사용 - 영화 길이가 100 ~ 180분 사이인 영화 조회
            """)
    @Test
    void 컴포지션_dao_1() {
        // given
        var start = 100;
        var end = 180;

        // when
        List<Film> films = filmRepositoryHasA.findByRangeBetween(start, end);

        // then
        assertThat(films).allSatisfy(film ->
                assertThat(film.getLength()).isBetween(start, end)
        );
    }
}
