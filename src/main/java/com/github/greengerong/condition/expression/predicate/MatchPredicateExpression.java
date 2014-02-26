package com.github.greengerong.condition.expression.predicate;

import com.google.common.base.Predicate;

import java.util.regex.Pattern;

public class MatchPredicateExpression implements Predicate<String> {
    private String pattern;

    public MatchPredicateExpression(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean apply(String input) {
        return input == null ? false : Pattern.compile(pattern).matcher(input).find();
    }
}
