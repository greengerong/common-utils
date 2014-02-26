package com.github.greengerong.condition.expression;

import com.google.common.base.Predicate;

public class ContainsStringPredicateExpression implements Predicate<String> {
    private String subStr;

    public ContainsStringPredicateExpression(String substr) {
        this.subStr = substr;
    }

    @Override
    public boolean apply(String input) {
        return input == null ? false : input.contains(subStr);
    }
}
