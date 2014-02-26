package com.github.greengerong.condition;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

class WhenGroup {

    private Predicate predicate;
    private Function function;

    public WhenGroup(Predicate predicate, Function function) {

        this.predicate = predicate;
        this.function = function;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public Function getFunction() {
        return function;
    }

    public <T> Object exec(T instance) {
        return getFunction().apply(instance);
    }
}
