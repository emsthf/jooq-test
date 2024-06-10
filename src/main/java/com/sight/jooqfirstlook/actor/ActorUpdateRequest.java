package com.sight.jooqfirstlook.actor;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ActorUpdateRequest {

    private String firstName;
    private String lastName;

    @Builder
    private ActorUpdateRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
