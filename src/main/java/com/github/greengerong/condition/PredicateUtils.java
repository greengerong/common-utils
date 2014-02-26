package com.github.greengerong.condition;

import com.google.common.base.Predicate;

public class PredicateUtils {

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

}
