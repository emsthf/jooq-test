package com.sight.jooqfirstlook.film;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JFilmActor;
import org.jooq.generated.tables.daos.FilmDao;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

// 상속을 통해서 dao 사용
@Repository
public class FilmRepositoryIsA extends FilmDao {

    private final DSLContext dslContext;
    private final JFilm FILM = JFilm.FILM;

    public FilmRepositoryIsA(Configuration configuration, DSLContext dslContext) {
        super(configuration);
        this.dslContext = dslContext;
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
                        DSL.row(FILM.fields()),
                        DSL.row(FILM_ACTOR.fields()),
                        DSL.row(ACTOR.fields())
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
}
