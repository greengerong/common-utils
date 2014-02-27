package com.github.greengerong.condition;


import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class Condition {

    private Condition() {
    }

    public static Condition when() {
        return new Condition();
    }

    public <T, E> ThenFunction then(final Predicate<T> predicate, final Function<T, E> function) {
        return ThenFunction.create().then(predicate, function);
    }
}

