package com.github.greengerong.condition.function;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import static com.github.greengerong.checker.Assert.checkNotNull;

public class ThenFunction {

    private List<WhenFunctionGroup> conditions = Lists.newArrayList();

    private ThenFunction() {
    }

    public static ThenFunction create() {
        return new ThenFunction();
    }

    public <T, E> ThenFunction then(Predicate<T> predicate, Function<T, E> function) {
        checkNotNull(predicate, "Predicate should be not null.");
        checkNotNull(function, "Function should be not null.");
        conditions.add(new WhenFunctionGroup(predicate, function));
        return this;
    }

    public <T, E> WhenFunctionDone otherwise(final Function<T, E> function) {
        checkNotNull(function, "Function should be not null.");
        return WhenFunctionDone.create(conditions, function);
    }

    public <T, E> E single(final T instance) {
        return WhenFunctionDone.create(conditions).single(instance);
    }

    public <T, E> List<E> all(final T instance) {
        return WhenFunctionDone.create(conditions).all(instance);
    }

}
