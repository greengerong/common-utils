package com.github.greengerong.condition.expression;

import com.google.common.base.Function;

public class WhenFunctionExpression {
    public static <T> Function returnWith(final T obj) {
        return new Function() {
            @Override
            public T apply(Object input) {
                return obj;
            }
        };
    }
}
