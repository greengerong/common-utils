package com.github.greengerong.collection;


import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface AggregateFunction2<E, T> {
    T apply(T input1, E input2);

    @Override
    boolean equals(Object object);
}
