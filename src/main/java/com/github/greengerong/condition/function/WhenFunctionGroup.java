package com.github.greengerong.condition.function;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

class WhenFunctionGroup {

    private Predicate predicate;
    private Function function;

    public WhenFunctionGroup(Predicate predicate, Function function) {

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
