package com.github.greengerong;


import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Function1<T> {

    void apply(T input);

    @Override
    boolean equals(Object object);
}
