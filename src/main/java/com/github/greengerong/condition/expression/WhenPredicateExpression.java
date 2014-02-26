package com.github.greengerong.condition.expression;

import com.google.common.base.Predicate;

import static com.google.common.base.Predicates.alwaysFalse;
import static com.google.common.base.Predicates.alwaysTrue;

public class WhenPredicateExpression {

    public static Predicate anything() {
        return alwaysTrue();
    }

    public static Predicate nothing() {
        return alwaysFalse();
    }

    public static Predicate<String> containsString(final String substr) {
        return new ContainsStringPredicateExpression(substr);
    }
}
