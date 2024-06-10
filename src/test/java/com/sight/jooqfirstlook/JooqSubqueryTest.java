package com.sight.jooqfirstlook;

import com.sight.jooqfirstlook.film.FilmPriceSummary;
import com.sight.jooqfirstlook.film.FilmRentalSummary;
import com.sight.jooqfirstlook.film.FilmRepository;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JooqSubqueryTest {

    @Autowired
    FilmRepository filmRepository;

    @DisplayName("""
            영화별 대여료가 
            1.0 이하면 'Cheap', 
            3.0 이하면 'Moderate',
            그 이상이면 'Expensive'로 분류하고, 
            각 영화의 총 재고 수를 조회한다.
            """)
    @Test
    void 스칼라_서브쿼리_예제() {
        // given
        String filmTitle = "EGG";

        // when
        List<FilmPriceSummary> priceSummaryList = filmRepository.findFilmPriceSummaryByFilmTitle(filmTitle);

        // then
        assertThat(priceSummaryList).isNotEmpty();
    }

    @DisplayName("평균 대여 기간이 가장 긴 영화부터 정렬해서 조회")
    @Test
    void from절_서브쿼리_예제() {
        // given
        String filmTitle = "EGG";

        // when
        List<FilmRentalSummary> rentalSummaryList = filmRepository.findFilmRentalSummaryByFilmTitle(filmTitle);

        // then
        assertThat(rentalSummaryList).isNotEmpty();
    }

    @DisplayName("대여된 기록이 있는 영화만 조회")
    @Test
    void 조건절_서브쿼리_예제() {
        // given
        String filmTitle = "EGG";

        // when
        List<Film> filmList = filmRepository.findRentedFilmByTitle(filmTitle);

        // then
        assertThat(filmList).isNotEmpty();
    }
}
