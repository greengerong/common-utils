package com.github.greengerong.condition.function;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import static com.github.greengerong.checker.Assert.checkNotNull;

public class WhenFunction {

    private List<WhenFunctionGroup> conditions = Lists.newArrayList();

    private WhenFunction() {
    }

    public static WhenFunction create() {
        return new WhenFunction();
    }

    public <T, E> WhenFunction when(Predicate<T> predicate, Function<T, E> function) {
        checkNotNull(predicate, "Predicate should be not null.");
        checkNotNull(function, "Function should be not null.");
        conditions.add(new WhenFunctionGroup(predicate, function));
        return this;
    }

    public <T, E> WhenFunctionDone otherwise(final Function<T, E> function) {
        checkNotNull(function, "Function should be not null.");
        return WhenFunctionDone.create(conditions, function);
    }

    public <T, E> E first(final T instance) {
        return WhenFunctionDone.create(conditions).first(instance);
    }

    public <T, E> List<E> pipe(final T instance) {
        return WhenFunctionDone.create(conditions).pipe(instance);
    }

}
