package com.sight.jooqfirstlook.config;

import org.jooq.conf.ExecuteWithoutWhere;
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
                    .withRenderSchema(false);
        };
    }
}
