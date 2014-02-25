package com.github.greengerong.collection;

import java.util.List;

public class Group<T, E> {
    private T key;
    private List<E> values;

    public Group(T key, List<E> values) {
        this.key = key;
        this.values = values;
    }

    public T getKey() {
        return key;
    }

    public List<E> getValues() {
        return values;
    }
}
