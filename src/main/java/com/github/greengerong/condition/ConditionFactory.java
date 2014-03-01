package com.github.greengerong.condition;


import com.github.greengerong.collection.Action;
import com.github.greengerong.condition.action.WhenAction;
import com.github.greengerong.condition.function.WhenFunction;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class ConditionFactory {

    private ConditionFactory() {
    }

    public static ConditionFactory condition() {
        return new ConditionFactory();
    }

    public <T, E> WhenFunction when(final Predicate<T> predicate, final Function<T, E> function) {
        return WhenFunction.create().when(predicate, function);
    }

    public <T> WhenAction when(final Predicate<T> predicate, final Action<T> function) {
        return WhenAction.create().when(predicate, function);
    }
}

