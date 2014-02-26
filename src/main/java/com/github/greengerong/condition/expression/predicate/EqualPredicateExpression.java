package com.github.greengerong.condition.expression.predicate;

import com.google.common.base.Predicate;

import java.lang.reflect.Array;

public class EqualPredicateExpression<T> implements Predicate<T> {
    private T expected;

    public EqualPredicateExpression(T expected) {
        this.expected = expected;
    }

    @Override
    public boolean apply(T input) {
        return areEqual(input, expected);
    }

    private static boolean areEqual(Object actual, Object expected) {
        if (actual == null) {
            return expected == null;
        }

        if (expected != null && isArray(actual)) {
            return isArray(expected) && areArraysEqual(actual, expected);
        }

        return actual.equals(expected);
    }

    private static boolean isArray(Object o) {
        return o.getClass().isArray();
    }

    private static boolean areArraysEqual(Object actualArray, Object expectedArray) {
        return areArrayLengthsEqual(actualArray, expectedArray) && areArrayElementsEqual(actualArray, expectedArray);
    }

    private static boolean areArrayLengthsEqual(Object actualArray, Object expectedArray) {
        return Array.getLength(actualArray) == Array.getLength(expectedArray);
    }

    private static boolean areArrayElementsEqual(Object actualArray, Object expectedArray) {
        for (int i = 0; i < Array.getLength(actualArray); i++) {
            if (!areEqual(Array.get(actualArray, i), Array.get(expectedArray, i))) {
                return false;
            }
        }
        return true;
    }
}
