package com.sight.jooqfirstlook;

import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.jooq.impl.DSL.dual;
import static org.jooq.impl.DSL.field;

@SpringBootTest
class JooqSlowQueryTest {

    @Autowired
    DSLContext dslContext;

    @DisplayName("SLOW 쿼리 탐지 테스트")
    @Test
    void 슬로우쿼리_탐지_테스트() {
        // given
        dslContext.select(field("SLEEP(4)"))
                .from(dual())
                .execute();

        // when

        // then

    }
}
