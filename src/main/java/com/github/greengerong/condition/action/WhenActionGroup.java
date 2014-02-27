package com.github.greengerong.condition.action;

import com.github.greengerong.collection.Action;
import com.google.common.base.Predicate;

class WhenActionGroup {

    private Predicate predicate;
    private Action action;

    public WhenActionGroup(Predicate predicate, Action action) {

        this.predicate = predicate;
        this.action = action;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public Action getAction() {
        return action;
    }

    public <T> boolean exec(T instance) {
        getAction().apply(instance);
        return true;
    }
}
