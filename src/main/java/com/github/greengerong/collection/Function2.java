package com.github.greengerong.collection;


import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Function2<E> {
    E apply(E input1, E input2);

    @Override
    boolean equals(Object object);
}
