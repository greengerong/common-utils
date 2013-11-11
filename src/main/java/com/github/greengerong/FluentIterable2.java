package com.github.greengerong;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

import java.util.*;

import static com.google.common.base.Defaults.defaultValue;
import static com.google.common.base.Preconditions.checkNotNull;

public class FluentIterable2<E> extends FluentIterable<E> {

    private final Iterable<E> iterable;

    private FluentIterable2() {
        this.iterable = this;
    }

    protected FluentIterable2(Iterable<E> iterable) {
        this.iterable = checkNotNull(iterable);
    }

    @Override
    public Iterator<E> iterator() {
        return this.iterable.iterator();
    }

    public static <E> FluentIterable2<E> from(final Iterable<E> iterable) {
        return (iterable instanceof FluentIterable2) ? (FluentIterable2<E>) iterable
                : new FluentIterable2<E>(iterable);
    }

    public <T> FluentIterable2<T> select(Function<? super E, T> function) {
        return new FluentIterable2<T>(transform(function));
    }


    public FluentIterable2<E> where(Predicate<? super E> predicate) {
        return new FluentIterable2<E>(filter(predicate));
    }

    public <T> FluentIterable2<T> cast() {
        return select(new Function<E, T>() {
            @Override
            public T apply(E input) {
                return (T) (input);
            }
        });
    }

    public void forEach(Action<E> action) {
        final Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            action.apply(iterator.next());
        }
    }

    public <K, V> Map<K, V> toMap(final Function<? super E, K> keyFunction, final Function<? super E, V> valueFunction) {
        final HashMap<K, V> kvHashMap = new HashMap<K, V>();
        forEach(new Action<E>() {
            @Override
            public void apply(E input) {
                kvHashMap.put(keyFunction.apply(input), valueFunction.apply(input));
            }
        });

        return kvHashMap;
    }

    public FluentIterable2<E> distinct() {
        final Set<E> set = new LinkedHashSet<E>();
        forEach(new Action<E>() {
            @Override
            public void apply(E input) {
                set.add(input);
            }
        });
        return new FluentIterable2<E>(set);
    }


    public FluentIterable2<E> distinct(Comparator<E> comparator) {
        final Set<E> set = new TreeSet<E>(comparator);
        forEach(new Action<E>() {
            @Override
            public void apply(E input) {
                set.add(input);
            }
        });
        return new FluentIterable2<E>(set);
    }

    public FluentIterable2<E> distinct(final Function<E, Object> function) {
        return distinct(new Comparator<E>() {
            @Override
            public int compare(E e, E e2) {
                return function.apply(e).equals(function.apply(e2)) ? 0 : 1;
            }
        });
    }

    public E firstOrDefault(E defaultValue) {
        return first().or(defaultValue);
    }

    public <T> FluentIterable2<T> ofType(Class<T> type) {
        return new FluentIterable2<T>(filter(type)).cast();
    }

    public <T> FluentIterable<Group<T, E>> groupBy(final Function<E, T> function) {
        final HashMap<T, List<E>> group = Maps.newHashMap();
        forEach(new Action<E>() {
            @Override
            public void apply(E input) {
                final T key = function.apply(input);
                if (group.containsKey(key)) {
                    group.get(key).add(input);
                } else {
                    group.put(key, Lists.newArrayList(input));
                }
            }
        });

        return from(group.entrySet()).transform(new Function<Map.Entry<T, List<E>>, Group<T, E>>() {
            @Override
            public Group<T, E> apply(Map.Entry<T, List<E>> input) {
                return new Group(input.getKey(), input.getValue());
            }
        });
    }

    public FluentIterable2<E> orderBy(Comparator<? super E> comparator) {
        return new FluentIterable2<E>(toSortedList(comparator));
    }

    public FluentIterable2<E> orderBy() {
        return orderBy(new Comparator<E>() {
            @Override
            public int compare(E e, E e2) {
                return ((Comparable) e).compareTo(e2);
            }
        });
    }

    public <T> FluentIterable2<E> orderBy(final Function<E, ? super Comparable<T>> comparable) {
        return orderBy(new Comparator<E>() {
            @Override
            public int compare(E e, E e2) {
                final Comparable<T> p1 = (Comparable<T>) comparable.apply(e);
                final T p2 = (T) comparable.apply(e2);
                return p1.compareTo(p2);
            }
        });
    }

    public FluentIterable2<E> skipWhere(Predicate<E> predicate) {
        final Iterator<E> iterator = this.iterator();
        E d = null;
        while (iterator.hasNext() && !predicate.apply(d = iterator.next())) ;
        final List<E> list = Lists.newArrayList();
        if (d != null) {
            list.add(d);
        }
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return new FluentIterable2<E>(list);
    }


    public E aggregate(final Class<E> type, final Function2<E> function) {
        return aggregate(defaultValue(type), function);
    }

    public E aggregate(final E first, final Function2<E> function) {
        final Iterator<E> iterator = this.iterator();
        E lastValue = first;
        while (iterator.hasNext()) {
            final E next = iterator.next();
            lastValue = function.apply(lastValue, next);
        }
        return lastValue;
    }

    public FluentIterable2<E> take(int count) {
        return new FluentIterable2<E>(limit(count));
    }

    public FluentIterable2<E> takeWhere(Predicate<E> predicate) {
        final List<E> list = Lists.newArrayList();
        final Iterator<E> iterator = iterator();
        E d = null;
        while (iterator.hasNext() && !predicate.apply(d = iterator.next())) {
            list.add(d);
        }
        return new FluentIterable2<E>(list);
    }

    public <T, R> FluentIterable2<R> zip(final Iterable<T> secondIterable, final Function3<E, T, R> function) {
        checkNotNull(secondIterable);
        final List<E> first = Lists.newArrayList(iterable);
        final List<T> second = Lists.newArrayList(secondIterable);
        final List<R> results = Lists.newArrayList();
        int len = Math.min(first.size(), second.size());
        for (int i = 0; i < len; i++) {
            final R result = function.apply(first.get(i), second.get(i));
            results.add(result);

        }
        return new FluentIterable2<R>(results);
    }

}
