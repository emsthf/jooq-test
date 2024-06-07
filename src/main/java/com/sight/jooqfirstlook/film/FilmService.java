package com.sight.jooqfirstlook.film;

import com.sight.jooqfirstlook.web.FilmWithActorPagedResponse;
import com.sight.jooqfirstlook.web.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmWithActorPagedResponse getFilmActorPageResponse(Long page, Long pageSize) {
        List<FilmWithActor> filmWithActorList = filmRepository.findFilmWithActorList(page, pageSize);
        PagedResponse pagedResponse = new PagedResponse(page, pageSize);

        return new FilmWithActorPagedResponse(pagedResponse, filmWithActorList);
    }
}
