package com.github.greengerong.condition;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

import static com.github.greengerong.checker.Assert.checkNotNull;
import static com.google.common.collect.FluentIterable.from;

public class When {

    private List<WhenGroup> conditions = Lists.newArrayList();
    private Function otherwiseFunction;

    public When() {
    }

    public <T, E> When then(final Predicate<T> predicate, final Function<T, E> function) {
        checkNotNull(predicate, "Predicate should be not null.");
        checkNotNull(function, "Function should be not null.");

        this.conditions.add(new WhenGroup(predicate, function));
        return this;
    }

    public <T, E> When otherwise(final Function<T, E> function) {
        checkNotNull(function, "Function should be not null.");

        this.otherwiseFunction = function;
        return this;
    }

    public <T, E> E single(final T instance) {
        final Optional<WhenGroup> results = from(this.conditions).firstMatch(IsMatchCondition(instance));
        if (!results.isPresent() && otherwiseFunction != null) {
            return (E) execOtherwise(instance);
        }
        return results.isPresent() ? (E) results.get().exec(instance) : null;
    }

    public <T, E> List<E> all(final T instance) {
        final List<E> results = from(this.conditions)
                .filter(IsMatchCondition(instance))
                .transform(new Function<WhenGroup, E>() {
                    @Override
                    public E apply(WhenGroup input) {
                        return (E) input.exec(instance);
                    }

                }).toList();

        if (results.size() == 0 && otherwiseFunction != null) {
            return (List<E>) Lists.newArrayList(execOtherwise(instance));
        }

        return results;
    }

    private Object execOtherwise(Object instance) {
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

    private class WhenGroup {

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
}
