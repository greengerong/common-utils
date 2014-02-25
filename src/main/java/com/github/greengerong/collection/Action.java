package com.github.greengerong.collection;


import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Action<T> {

    void apply(T input);

    @Override
    boolean equals(Object object);
}
