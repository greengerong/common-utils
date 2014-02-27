package com.github.greengerong.condition;


import com.github.greengerong.collection.Action;
import com.github.greengerong.condition.action.ThenAction;
import com.github.greengerong.condition.function.ThenFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class WhenFactory {

    private WhenFactory() {
    }

    public static WhenFactory when() {
        return new WhenFactory();
    }

    public <T, E> ThenFunction then(final Predicate<T> predicate, final Function<T, E> function) {
        return ThenFunction.create().then(predicate, function);
    }

    public <T> ThenAction then(final Predicate<T> predicate, final Action<T> function) {
        return ThenAction.create().then(predicate, function);
    }
}

