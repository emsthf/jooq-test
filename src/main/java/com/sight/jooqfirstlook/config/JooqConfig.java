package com.sight.jooqfirstlook.config;

import org.jooq.conf.ExecuteWithoutWhere;
import org.jooq.conf.RenderImplicitJoinType;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public DefaultConfigurationCustomizer jooqDefaultConfigurationCustomizer() {
        return conf -> {
            conf.set(PerformanceListener::new);  // 슬로우 쿼리 탐지를 위한 내 커스텀 리스너 추가
            conf.settings()
                    .withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW)  // delete문에 where절이 없을 때 예외를 던지도록 설정
                    .withExecuteUpdateWithoutWhere(ExecuteWithoutWhere.THROW)  // update문에 where절이 없을 때 예외를 던지도록 설정
                    .withRenderSchema(false)

                    // implicit path join to-many는 기본적으로 에러를 발생시켜 이렇게 수동으로 조인을 지정 해야한다.
                    .withRenderImplicitJoinToManyType(RenderImplicitJoinType.THROW)

                    // implicit PATH JOIN many-to-one 을 비활성화 하고 싶다면 하고 싶다면
                    .withRenderImplicitJoinType(RenderImplicitJoinType.THROW)
            ;
        };
    }
}
