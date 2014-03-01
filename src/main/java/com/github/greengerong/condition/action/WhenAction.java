package com.github.greengerong.condition.action;

import com.github.greengerong.collection.Action;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.util.List;

import static com.github.greengerong.checker.Assert.checkNotNull;

public class WhenAction {

    private List<WhenActionGroup> conditions = Lists.newArrayList();

    private WhenAction() {
    }

    public static WhenAction create() {
        return new WhenAction();
    }

    public <T> WhenAction when(Predicate<T> predicate, Action<T> action) {
        checkNotNull(predicate, "Predicate should be not null.");
        checkNotNull(action, "Action should be not null.");
        conditions.add(new WhenActionGroup(predicate, action));
        return this;
    }

    public <T> WhenActionDone otherwise(final Action<T> action) {
        checkNotNull(action, "Action should be not null.");
        return WhenActionDone.create(conditions, action);
    }

    public <T> void first(final T instance) {
        WhenActionDone.create(conditions).first(instance);
    }

    public <T> void pipe(final T instance) {
        WhenActionDone.create(conditions).pipe(instance);
    }
}
