package com.github.greengerong.condition;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;

public class WhenDone {

    private final List<WhenGroup> conditions;
    private final Function otherwiseFunction;

    private WhenDone(final List<WhenGroup> conditions, final Function otherwiseFunction) {

        this.conditions = conditions;
        this.otherwiseFunction = otherwiseFunction;
    }

    private WhenDone(final List<WhenGroup> conditions) {
        this(conditions, null);
    }

    static WhenDone create(final List<WhenGroup> conditions, final Function otherwiseFunction) {
        return new WhenDone(conditions, otherwiseFunction);
    }

    static WhenDone create(final List<WhenGroup> conditions) {
        return new WhenDone(conditions);
    }

    public <T, E> E single(final T instance) {
        final Optional<WhenGroup> results = from(conditions).firstMatch(IsMatchCondition(instance));
        if (!results.isPresent() && otherwiseFunction != null) {
            return (E) execOtherwise(otherwiseFunction, instance);
        }
        return results.isPresent() ? (E) results.get().exec(instance) : null;
    }

    public <T, E> List<E> all(final T instance) {
        final List<E> results = from(conditions)
                .filter(IsMatchCondition(instance))
                .transform(new Function<WhenGroup, E>() {
                    @Override
                    public E apply(WhenGroup input) {
                        return (E) input.exec(instance);
                    }

                }).toList();

        if (results.size() == 0 && otherwiseFunction != null) {
            return (List<E>) Lists.newArrayList(execOtherwise(otherwiseFunction, instance));
        }

        return results;
    }

    private Object execOtherwise(final Function otherwiseFunction, final Object instance) {
        return otherwiseFunction.apply(instance);
    }

    private <T> Predicate<WhenGroup> IsMatchCondition(final T instance) {
        return new Predicate<WhenGroup>() {
            @Override
            public boolean apply(WhenGroup input) {
                return input.getPredicate().apply(instance);
            }
        };
    }
}
