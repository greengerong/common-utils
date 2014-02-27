package com.github.greengerong.condition.action;

import com.github.greengerong.collection.Action;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import static com.github.greengerong.checker.Assert.checkNotNull;

public class ThenAction {

    private List<WhenActionGroup> conditions = Lists.newArrayList();

    private ThenAction() {
    }

    public static ThenAction create() {
        return new ThenAction();
    }

    public <T> ThenAction then(Predicate<T> predicate, Action<T> action) {
        checkNotNull(predicate, "Predicate should be not null.");
        checkNotNull(action, "Action should be not null.");
        conditions.add(new WhenActionGroup(predicate, action));
        return this;
    }

    public <T> WhenActionDone otherwise(final Action<T> action) {
        checkNotNull(action, "Action should be not null.");
        return WhenActionDone.create(conditions, action);
    }

    public <T> void single(final T instance) {
        WhenActionDone.create(conditions).single(instance);
    }

    public <T> void all(final T instance) {
        WhenActionDone.create(conditions).all(instance);
    }
}
