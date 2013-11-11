package com.github.greengerong;


import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Function3<E, T, R> {
    R apply(E input1, T input2);

    @Override
    boolean equals(Object object);
}
