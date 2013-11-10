package com.github.greengerong;


import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Action<T> {

    void apply(T input);

    @Override
    boolean equals(Object object);
}
