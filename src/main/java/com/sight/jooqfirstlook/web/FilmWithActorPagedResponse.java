package com.sight.jooqfirstlook.web;

import com.sight.jooqfirstlook.film.FilmWithActor;
import lombok.Getter;

import java.util.List;

@Getter
public class FilmWithActorPagedResponse {

    private PagedResponse page;
    private List<FilmActorResponse> filmActorList;

    public FilmWithActorPagedResponse(PagedResponse page, List<FilmWithActor> filmWithActorList) {
        this.page = page;
        this.filmActorList = filmWithActorList.stream()
                .map(FilmActorResponse::new)
                .toList();
    }

    @Getter
    public static class FilmActorResponse {

        private final String filmTitle;
        private final String actorFullName;
        private final Long filmId;

        public FilmActorResponse(FilmWithActor filmWithActor) {
            this.filmTitle = filmWithActor.getTitle();
            this.actorFullName = filmWithActor.getActorFullName();
            this.filmId = filmWithActor.getFilmId();
        }
    }
}
