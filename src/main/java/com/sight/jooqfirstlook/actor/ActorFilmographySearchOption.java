package com.sight.jooqfirstlook.actor;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActorFilmographySearchOption {

    private final String actorName;

    private final String filmTitle;
}
