package com.github.greengerong.condition.expression;

import com.google.common.base.Predicate;

public class WhenExpression {

    public static final Predicate TRUE = new Predicate() {
        @Override
        public boolean apply(Object input) {
            return true;
        }
    };

    public static final Predicate FALSE = new Predicate() {
        @Override
        public boolean apply(Object input) {
            return false;
        }
    };

    public static Predicate anything() {
        return TRUE;
    }

    public static <T> Predicate<T> eq(final T obj) {
        return new EqualExpression(obj);
    }

    public static <T> Predicate<T> instanceOf(final T obj) {
        return new InstanceOfExpression(obj);
    }


}