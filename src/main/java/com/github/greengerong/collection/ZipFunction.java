package com.github.greengerong.collection;


import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface ZipFunction<E, T, R> {
    R apply(E input1, T input2);

    @Override
    boolean equals(Object object);
}
