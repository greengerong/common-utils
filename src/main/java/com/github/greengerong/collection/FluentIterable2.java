package com.github.greengerong.collection;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

    public void forEach(final Action<E> action) {
        transform(new Function<E, Boolean>() {
            @Override
            public Boolean apply(E input) {
                action.apply(input);
                return true;
            }
        }).toList();
    }

    public <K, V> Map<K, V> toMap(final Function<? super E, K> keyFunction,
                                  final Function<? super E, V> valueFunction) {
        return aggregate(new HashMap<K, V>(), new AggregateFunction<E, Map<K, V>>() {
            @Override
            public Map<K, V> apply(Map<K, V> map, E input) {
                map.put(keyFunction.apply(input), valueFunction.apply(input));
                return map;
            }
        });
    }

    public FluentIterable2<E> distinct() {

        final Set<E> set = aggregate(new LinkedHashSet<E>(),
                new AggregateFunction<E, Set<E>>() {
                    @Override
                    public Set<E> apply(Set<E> set, E input) {
                        set.add(input);
                        return set;
                    }
                });
        return new FluentIterable2<E>(set);
    }


    public FluentIterable2<E> distinct(Comparator<E> comparator) {
        final Set<E> set = aggregate(new TreeSet<E>(comparator),
                new AggregateFunction<E, Set<E>>() {
                    @Override
                    public Set<E> apply(Set<E> set, E input) {
                        set.add(input);
                        return set;
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

    public E firstOrDefault() {
        return first().orNull();
    }

    public E firstOrDefault(Predicate<E> predicate) {
        return super.firstMatch(predicate).orNull();
    }

    public E firstOrDefault(E defaultValue, Predicate<E> predicate) {
        return super.firstMatch(predicate).or(defaultValue);
    }

    public <T> FluentIterable2<T> ofType(Class<T> type) {
        return new FluentIterable2<T>(filter(type)).cast();
    }

    public <T> FluentIterable<Group<T, E>> groupBy(final Function<E, T> function) {
        final Map<T, List<E>> group = Maps.newHashMap();
        aggregate(group, new AggregateFunction<E, Map<T, List<E>>>() {
            @Override
            public Map<T, List<E>> apply(Map<T, List<E>> group, E input) {
                final T key = function.apply(input);
                if (group.containsKey(key)) {
                    group.get(key).add(input);
                } else {
                    group.put(key, Lists.newArrayList(input));
                }
                return group;
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


    public <T> T aggregate(final Class<T> type, final AggregateFunction<E, T> function) {
        return aggregate(defaultValue(type), function);
    }

    public <T> T aggregate(final T first, final AggregateFunction<E, T> function) {
        final Iterator<E> iterator = this.iterator();
        T lastValue = first;
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
        E d;
        while (iterator.hasNext() && !predicate.apply(d = iterator.next())) {
            list.add(d);
        }
        return new FluentIterable2<E>(list);
    }

    public <T, R> FluentIterable2<R> zip(final Iterable<T> secondIterable, final ZipFunction<E, T, R> function) {
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

    public FluentIterable2<E> skip2(int count) {
        return new FluentIterable2<E>(super.skip(count));
    }

    public FluentIterable2<E> flatten() {
        final List<E> flattenResult = flatten(Lists.newArrayList(iterable));
        final LinkedList<E> result = Lists.newLinkedList(flattenResult);
        return new FluentIterable2<E>(result);
    }

    private List<E> flatten(List<E> flattenList) {
        final List<E> list = Lists.newArrayList();
        for (E item : flattenList) {
            if (item instanceof Iterable) {
                list.addAll(flatten(Lists.newArrayList((Iterable<E>) item)));
            } else {
                list.add(item);
            }
        }
        return list;
    }

}
