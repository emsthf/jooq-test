package com.sight.jooqfirstlook.film;

import com.sight.jooqfirstlook.config.converter.PriceCategoryConverter;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.generated.tables.*;
import org.jooq.generated.tables.pojos.Film;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
public class FilmRepository {

    private final DSLContext dslContext;
    private final JFilm FILM = JFilm.FILM;

    public Film findById(Long id) {
        return dslContext.select(FILM.fields())
                .from(FILM)
                .where(FILM.FILM_ID.eq(id))
                .fetchOneInto(Film.class);  // fetch()는 여러 row 반환. 그리고 반환 타입은 Record
    }

    public SimpleFilmInfo findSimpleFilmInfoById(Long id) {
        return dslContext.select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.DESCRIPTION
                )
                .from(FILM)
                .where(FILM.FILM_ID.eq(id))
                .fetchOneInto(SimpleFilmInfo.class);
    }

    public List<FilmWithActor> findFilmWithActorList(Long page, Long pageSize) {
        JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
        JActor ACTOR = JActor.ACTOR;

        return dslContext.select(
                        row(FILM.fields()),
                        row(FILM_ACTOR.fields()),
                        row(ACTOR.fields())
                )
                .from(FILM)
                .join(FILM_ACTOR)
                .on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
                .join(ACTOR)
                .on(FILM_ACTOR.ACTOR_ID.eq(ACTOR.ACTOR_ID))
                .offset((page - 1) * pageSize)
                .limit(pageSize)
                .fetchInto(FilmWithActor.class);
    }

    public List<FilmPriceSummary> findFilmPriceSummaryByFilmTitle(String filmTitle) {
        final JInventory INVENTORY = JInventory.INVENTORY;

        return dslContext.select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.RENTAL_RATE,
                        case_()
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(1.0)), "Cheap")
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(3.0)), "Moderate")
                                .else_("Expensive").as("priceCategory").convert(new PriceCategoryConverter()),
                        selectCount().from(INVENTORY).where(INVENTORY.FILM_ID.eq(FILM.FILM_ID)).asField("total_inventory")
                )
                .from(FILM)
                .where(FILM.TITLE.like("%" + filmTitle + "%"))
                .fetchInto(FilmPriceSummary.class);
    }

    public List<FilmRentalSummary> findFilmRentalSummaryByFilmTitle(String filmTitle) {
        final JInventory INVENTORY = JInventory.INVENTORY;
        final JRental RENTAL = JRental.RENTAL;

        var rentalDurationInfoSubquery = select(
                INVENTORY.FILM_ID,
                avg(localDateTimeDiff(DatePart.DAY, RENTAL.RENTAL_DATE, RENTAL.RETURN_DATE)).as("average_rental_duration")
        )
                .from(RENTAL)
                .join(INVENTORY)
                .on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
                .where(RENTAL.RETURN_DATE.isNotNull())
                .groupBy(INVENTORY.FILM_ID)
                .asTable("rental_duration_info");

        return dslContext.select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        rentalDurationInfoSubquery.field("average_rental_duration")
                )
                .from(FILM)
                .join(rentalDurationInfoSubquery)
                .on(FILM.FILM_ID.eq(rentalDurationInfoSubquery.field(FILM.FILM_ID)))
                .where(FILM.TITLE.like("%" + filmTitle + "%"))
                .orderBy(field(name("average_rental_duration")).desc())
                .fetchInto(FilmRentalSummary.class);
    }

    public List<Film> findRentedFilmByTitle(String filmTitle) {
        final JInventory INVENTORY = JInventory.INVENTORY;
        final JRental RENTAL = JRental.RENTAL;

        return dslContext.selectFrom(FILM)
                .whereExists(
                        selectOne()
                                .from(INVENTORY)
                                .join(RENTAL)
                                .on(INVENTORY.INVENTORY_ID.eq(RENTAL.INVENTORY_ID))
                                .where(INVENTORY.FILM_ID.eq(FILM.FILM_ID))
                ).and(FILM.TITLE.like("%" + filmTitle + "%"))
                .fetchInto(Film.class);
    }
}
