package com.sight.jooqfirstlook.config;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Query;
import org.jooq.tools.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class PerformanceListener implements ExecuteListener {

    private static final Logger log = LoggerFactory.getLogger(PerformanceListener.class);
    private StopWatch watch;

    private static final Duration SLOW_QUERY_LIMIT = Duration.ofSeconds(3);

    @Override
    public void executeStart(ExecuteContext ctx) {
        watch = new StopWatch();
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        final long queryTimeNano = watch.split();
        if (queryTimeNano > SLOW_QUERY_LIMIT.getNano()) {
            Query query = ctx.query();// 컨텍스트에서 실행한 SQL을 가져와
            Duration executeTime = Duration.ofNanos(queryTimeNano);
            log.warn(String.format(
                    """
                            ### Slow SQL 탐지 >> 
                            경고: jOOQ로 실행된 쿼리 중 %d초 이상 실행된 쿼리가 있습니다. 
                            실행시간: %s초
                            실행쿼리: %s
                            """,
                    SLOW_QUERY_LIMIT.toSeconds(),
                    millisToSeconds(executeTime),
                    query
            ));
        }
    }

    private String millisToSeconds(Duration duration) {
        return String.format("%.1f", duration.toMillis() / 1000.0);
    }
}
