package com.sight.jooqfirstlook.actor;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JFilmActor;
import org.jooq.generated.tables.daos.ActorDao;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.generated.tables.records.ActorRecord;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.sight.jooqfirstlook.utils.jooq.JooqListConditionUtil.inIfNotEmpty;
import static org.jooq.impl.DSL.*;

@Repository
public class ActorRepository {

    private final DSLContext dslContext;
    private final ActorDao actorDao;
    private final JActor ACTOR = JActor.ACTOR;

    public ActorRepository(DSLContext dslContext, Configuration configuration) {
        this.actorDao = new ActorDao(configuration);
        this.dslContext = dslContext;
    }

    public List<Actor> findByFirstNameAndLastName(String fistName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(
                        ACTOR.FIRST_NAME.eq(fistName),
                        ACTOR.LAST_NAME.eq(lastName)
                )
                .fetchInto(Actor.class);
    }

    public List<Actor> findByFirstNameOrLastName(String fistName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(
                        ACTOR.FIRST_NAME.eq(fistName).or(ACTOR.LAST_NAME.eq(lastName))
                )
                .fetchInto(Actor.class);
    }

    public List<Actor> findByActorIdIn(List<Long> idList) {
        // 만약 파라미터인 idList가 null이거나 empty list라면, where절에 false가 들어가 아무것도 가져오지 않는다.
        // 파라미터인 idList가 null이거나 empty list라면, 조건절을 무시하도록 해보자.
        return dslContext.selectFrom(ACTOR)
                .where(inIfNotEmpty(ACTOR.ACTOR_ID, idList))
                .fetchInto(Actor.class);
    }

    public List<ActorFilmography> findActorFilmography(ActorFilmographySearchOption searchOption) {
        final JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
        final JFilm FILM = JFilm.FILM;

        Map<Actor, List<Film>> actorListMap = dslContext.select(
                        row(ACTOR.fields()).as("actor"),
                        row(FILM.fields()).as("film")
                )
                .from(ACTOR)
                .join(FILM_ACTOR)
                .on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
                .join(FILM)
                .on(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
                .where(
                        containsIfNotBlank(ACTOR.FIRST_NAME.concat(" ").concat(ACTOR.LAST_NAME), searchOption.getActorName()),
                        containsIfNotBlank(FILM.TITLE, searchOption.getFilmTitle())
                )
                .fetchGroups(
                        record -> record.get("actor", Actor.class),
                        record -> record.get("film", Film.class)
                );

        return actorListMap.entrySet().stream()
                .map(entry -> new ActorFilmography(entry.getKey(), entry.getValue()))
                .toList();
    }

    private Condition containsIfNotBlank(Field<String> field, String inputValue) {
        if (inputValue == null || inputValue.isBlank()) {
            return noCondition();
        }
        return field.like("%" + inputValue + "%");
    }

    /**
     * 이 부분이 지원되기까지 굉장히 많은 논의가 있었음
     * jOOQ 3.19 부터 지원
     * <p>
     * 참고) https://github.com/jOOQ/jOOQ/issues/2536
     *
     * @return insert 시에 생성된 PK 값이 세팅된 pojo
     */
    public Actor saveByDao(Actor actor) {

        // 이때 PK(actorId)가 actor 객체에 추가됨
        actorDao.insert(actor);
        return actor;
    }

    public ActorRecord saveByRecord(Actor actor) {
        ActorRecord actorRecord = dslContext.newRecord(ACTOR, actor);
        actorRecord.insert();
        return actorRecord;
    }

    public Long saveWithReturningPkOnly(Actor actor) {
        return dslContext.insertInto(
                        ACTOR,
                        ACTOR.FIRST_NAME,
                        ACTOR.LAST_NAME
                )
                .values(
                        actor.getFirstName(),
                        actor.getLastName()
                )
                .returningResult(ACTOR.ACTOR_ID)
                .fetchOneInto(Long.class);
    }

    public Actor saveWithReturning(Actor actor) {
        return dslContext.insertInto(
                        ACTOR,
                        ACTOR.FIRST_NAME,
                        ACTOR.LAST_NAME
                )
                .values(
                        actor.getFirstName(),
                        actor.getLastName()
                )
                .returningResult(ACTOR.fields())  // returning이 될 때 insert된 pk를 가지고 다시 조회를 함(총 2개의 쿼리가 나감)
                .fetchOneInto(Actor.class);
    }

    public void bulkInsertWithRows(List<Actor> actorList) {
        var rows = actorList.stream()
                .map(actor -> row(
                        actor.getFirstName(),
                        actor.getLastName()
                ))
                .toList();

        dslContext.insertInto(
                        ACTOR,
                        ACTOR.FIRST_NAME,
                        ACTOR.LAST_NAME
                )
                .valuesOfRows(rows)
                .execute();
    }

    public void update(Actor actor) {
        actorDao.update(actor);
    }

    public Actor findByActorId(Long actorId) {
        return actorDao.findById(actorId);
    }

    public int updateWithDto(Long newActorId, ActorUpdateRequest request) {
        var firstName = StringUtils.hasText(request.getFirstName()) ? val(request.getFirstName()) : noField(ACTOR.FIRST_NAME);
        var lastName = StringUtils.hasText(request.getLastName()) ? val(request.getLastName()) : noField(ACTOR.LAST_NAME);

        return dslContext.update(ACTOR)
                .set(ACTOR.FIRST_NAME, firstName)
                .set(ACTOR.LAST_NAME, lastName)
                .where(ACTOR.ACTOR_ID.eq(newActorId))
                .execute();
    }

    public int updateWithRecord(Long newActorId, ActorUpdateRequest request) {
        ActorRecord record = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(newActorId));

        if (StringUtils.hasText(request.getFirstName())) {
            record.setFirstName(request.getFirstName());
        }

        if (StringUtils.hasText(request.getLastName())) {
            record.setLastName(request.getLastName());
        }

        return dslContext.update(ACTOR)
                .set(record)
                .where(ACTOR.ACTOR_ID.eq(newActorId))
                .execute();
    }

    public int delete(Long newActorId) {
        return dslContext.deleteFrom(ACTOR)
                .where(ACTOR.ACTOR_ID.eq(newActorId))
                .execute();
    }

    public int deleteWithRecord(Long newActorId) {
        ActorRecord record = dslContext.fetchOne(ACTOR, ACTOR.ACTOR_ID.eq(newActorId));

        return record.delete();
    }
}
