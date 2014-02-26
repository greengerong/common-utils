package com.github.greengerong.condition.expression;

import com.github.greengerong.condition.expression.predicate.EqualPredicateExpression;
import com.github.greengerong.condition.expression.predicate.InstanceOfPredicateExpression;
import com.github.greengerong.condition.expression.predicate.MatchPredicateExpression;
import com.google.common.base.Predicate;

public class WhenPredicateExpression {

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

    public static Predicate nothing() {
        return FALSE;
    }

    public static <T> Predicate<T> eq(final T obj) {
        return new EqualPredicateExpression(obj);
    }

    public static <T> Predicate<T> instanceOf(final T obj) {
        return new InstanceOfPredicateExpression(obj);
    }

    public static Predicate<String> match(final String pattern) {
        return new MatchPredicateExpression(pattern);
    }

    public static Predicate<String> contains(final String substr) {
        return new ContainsStringPredicateExpression(substr);
    }
}
