package com.sight.jooqfirstlook.utils.jooq;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class JooqListConditionUtil {

    public static <T> Condition inIfNotEmpty(Field<T> field, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return DSL.noCondition();  // noCondition()을 where절에서 받으 이 조건절을 제외하고 렌더링
        }
        return field.in(idList);
    }
}
