package com.github.greengerong.condition;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import static com.github.greengerong.checker.Assert.checkNotNull;

public class ThenFunction {

    private List<WhenGroup> conditions = Lists.newArrayList();

    private ThenFunction() {
    }

    public static ThenFunction create() {
        return new ThenFunction();
    }

    public <T, E> ThenFunction then(Predicate<T> predicate, Function<T, E> function) {
        checkNotNull(predicate, "Predicate should be not null.");
        checkNotNull(function, "Function should be not null.");
        conditions.add(new WhenGroup(predicate, function));
        return this;
    }

    public <T, E> WhenDone otherwise(final Function<T, E> function) {
        checkNotNull(function, "Function should be not null.");
        return WhenDone.create(conditions, function);
    }

    public <T, E> E single(final T instance) {
        return WhenDone.create(conditions).single(instance);
    }

    public <T, E> List<E> all(final T instance) {
        return WhenDone.create(conditions).all(instance);
    }
}
