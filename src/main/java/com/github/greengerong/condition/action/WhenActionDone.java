package com.github.greengerong.condition.action;

import com.github.greengerong.collection.Action;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;

public class WhenActionDone {
    private List<WhenActionGroup> conditions;
    private Action otherwiseFunction;

    public WhenActionDone(List<WhenActionGroup> conditions, Action otherwiseFunction) {
        this.conditions = conditions;
        this.otherwiseFunction = otherwiseFunction;
    }

    public WhenActionDone(List<WhenActionGroup> conditions) {
        this(conditions, null);
    }

    public static <T> WhenActionDone create(List<WhenActionGroup> conditions, Action<T> action) {
        return new WhenActionDone(conditions, action);
    }


    public <T> void single(final T instance) {
        final Optional<WhenActionGroup> results = from(conditions).firstMatch(IsMatchCondition(instance));
        if (!results.isPresent() && otherwiseFunction != null) {
            execOtherwise(otherwiseFunction, instance);
        }

        if (results.isPresent()) {
            results.get().exec(instance);
        }
    }

    public <T> void all(final T instance) {
        final List<Boolean> results = from(conditions)
                .filter(IsMatchCondition(instance))
                .transform(new Function<WhenActionGroup, Boolean>() {
                    @Override
                    public Boolean apply(WhenActionGroup input) {
                        return input.exec(instance);
                    }

                }).toList();

        if (results.size() == 0 && otherwiseFunction != null) {
            execOtherwise(otherwiseFunction, instance);
        }
    }

    private void execOtherwise(final Action otherwiseFunction, final Object instance) {
        otherwiseFunction.apply(instance);
    }

    private <T> Predicate<WhenActionGroup> IsMatchCondition(final T instance) {
        return new Predicate<WhenActionGroup>() {
            @Override
            public boolean apply(WhenActionGroup input) {
                return input.getPredicate().apply(instance);
            }
        };
    }

    public static WhenActionDone create(List<WhenActionGroup> conditions) {
        return new WhenActionDone(conditions);
    }
}
