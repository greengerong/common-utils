package com.github.greengerong.condition.expression;

import com.google.common.base.Predicate;

import static com.github.greengerong.checker.Assert.checkNotNull;

public class InstanceOfExpression<T> implements Predicate<T> {
    private T expectedClass;

    public InstanceOfExpression(T expectedClass) {
        this.expectedClass = expectedClass;
    }

    @Override
    public boolean apply(T input) {
        checkNotNull(input, "The input value should be not null value.");
        return matchableClass(input.getClass()).isInstance(expectedClass);
    }

    private static Class<?> matchableClass(Class<?> expectedClass) {
        if (boolean.class.equals(expectedClass)) return Boolean.class;
        if (byte.class.equals(expectedClass)) return Byte.class;
        if (char.class.equals(expectedClass)) return Character.class;
        if (double.class.equals(expectedClass)) return Double.class;
        if (float.class.equals(expectedClass)) return Float.class;
        if (int.class.equals(expectedClass)) return Integer.class;
        if (long.class.equals(expectedClass)) return Long.class;
        if (short.class.equals(expectedClass)) return Short.class;
        return expectedClass;
    }


}
