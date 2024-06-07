package com.sight.jooqfirstlook;

import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JOoqFirstLookApplicationTests {

    @Autowired
    DSLContext dslContext;

    @Test
    void selectWithJooq() {
        dslContext.selectFrom(JActor.ACTOR)
                .limit(10)
                .fetch();
    }
}
