package com.github.greengerong.condition.function;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;

public class WhenFunctionDone {

    private final List<WhenFunctionGroup> conditions;
    private final Function otherwiseFunction;

    private WhenFunctionDone(final List<WhenFunctionGroup> conditions, final Function otherwiseFunction) {

        this.conditions = conditions;
        this.otherwiseFunction = otherwiseFunction;
    }

    private WhenFunctionDone(final List<WhenFunctionGroup> conditions) {
        this(conditions, null);
    }

    static WhenFunctionDone create(final List<WhenFunctionGroup> conditions, final Function otherwiseFunction) {
        return new WhenFunctionDone(conditions, otherwiseFunction);
    }

    static WhenFunctionDone create(final List<WhenFunctionGroup> conditions) {
        return new WhenFunctionDone(conditions);
    }

    public <T, E> E single(final T instance) {
        final Optional<WhenFunctionGroup> results = from(conditions).firstMatch(IsMatchCondition(instance));
        if (!results.isPresent() && otherwiseFunction != null) {
            return (E) execOtherwise(otherwiseFunction, instance);
        }
        return results.isPresent() ? (E) results.get().exec(instance) : null;
    }

    public <T, E> List<E> all(final T instance) {
        final List<E> results = from(conditions)
                .filter(IsMatchCondition(instance))
                .transform(new Function<WhenFunctionGroup, E>() {
                    @Override
                    public E apply(WhenFunctionGroup input) {
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

    private <T> Predicate<WhenFunctionGroup> IsMatchCondition(final T instance) {
        return new Predicate<WhenFunctionGroup>() {
            @Override
            public boolean apply(WhenFunctionGroup input) {
                return input.getPredicate().apply(instance);
            }
        };
    }
}
