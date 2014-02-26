package com.github.greengerong.condition;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;

public class When {

    private List<WhenGroup> conditions = Lists.newArrayList();

    public When() {
    }

    public <T, E> When then(final Predicate<T> predicate, final Function<T, E> function) {
        this.conditions.add(new WhenGroup(predicate, function));
        return this;
    }

    public <T, E> E done(final T instance, final Class<E> outputType) {
        final List<E> results = done(instance, outputType, true);
        return results.size() > 0 ? results.get(0) : null;
    }

    public <T, E> List<E> done(final T instance, final Class<E> outputType, boolean first) {

        final List<WhenGroup> matchedConditions = Lists.newArrayList();
        final FluentIterable<WhenGroup> from = from(this.conditions);

        if (first) {
            final Optional<WhenGroup> group = from.firstMatch(IsMatchsCondtion(instance));
            if (group.isPresent()) {
                matchedConditions.add(group.get());
            }
        } else {
            matchedConditions.addAll(from.filter(IsMatchsCondtion(instance)).toList());
        }

        return from(matchedConditions).transform(new Function<WhenGroup, E>() {
            @Override
            public E apply(WhenGroup input) {
                return (E) input.getFunction().apply(instance);
            }
        }).toList();
    }

    private <T> Predicate<WhenGroup> IsMatchsCondtion(final T instance) {
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
    }
}
