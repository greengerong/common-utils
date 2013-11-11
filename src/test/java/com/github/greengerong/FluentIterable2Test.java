package com.github.greengerong;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.github.greengerong.FluentIterable2.from;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FluentIterable2Test {

    private ArrayList<Integer> dataLists;
    private ArrayList<People> peoples;

    @Before
    public void setUp() throws Exception {
        dataLists = Lists.newArrayList(1, 2, 3, 4, 9, 6, 3, 8, 9);
        peoples = new ArrayList<People>();
        peoples.add(new Student(1, "me"));
        peoples.add(new Student(9, "she"));
        peoples.add(new Student(9, "he"));
        peoples.add(new Student(5, "you"));
    }

    @Test
    public void shouldSelectByCondition() throws Exception {
        final List<Integer> mapLists = from(dataLists).select(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        }).toList();

        assertThat(mapLists.get(0), is(2));
        assertThat(mapLists.get(1), is(4));
        assertThat(mapLists.get(8), is(18));
    }

    @Test
    public void shouldWhereByCondition() throws Exception {
        final List<Integer> mapLists = from(dataLists).where(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        }).toList();

        assertThat(mapLists.size(), is(4));
        assertThat(mapLists.get(0), is(2));
        assertThat(mapLists.get(1), is(4));
        assertThat(mapLists.get(2), is(6));
        assertThat(mapLists.get(3), is(8));
    }

    @Test
    public void shouldCastAllElement() throws Exception {
        final FluentIterable2<Student> students = from(peoples).cast();

        assertThat(students.size(), is(4));

    }

    @Test
    public void shouldForEach() throws Exception {

        final List<Integer> lists = Lists.newArrayList();
        from(dataLists).forEach(new Action<Integer>() {
            @Override
            public void apply(Integer input) {
                if (input > 8) {
                    lists.add(input);
                }
            }
        });

        assertThat(lists.size(), is(2));
        assertThat(lists.get(0), is(9));
        assertThat(lists.get(1), is(9));
    }

    @Test
    public void shouldToMap() throws Exception {
        final Map<Integer, String> map = from(peoples).toMap(
                new Function<People, Integer>() {
                    @Override
                    public Integer apply(People input) {
                        return input.getId();
                    }
                }, new Function<People, String>() {
                    @Override
                    public String apply(People input) {
                        return input.getName();
                    }
                }
        );

        assertThat(map.get(1), is("me"));
        assertThat(map.get(5), is("you"));
    }

    @Test
    public void shouldDistinct() throws Exception {
        final FluentIterable2<Integer> distinct = from(dataLists).distinct();
        //1, 2, 3, 4, 9, 6, 3, 8, 9
        assertThat(distinct.size(), is(7));
        assertThat(distinct.get(0), is(1));
        assertThat(distinct.get(1), is(2));
        assertThat(distinct.get(2), is(3));
        assertThat(distinct.get(3), is(4));
        assertThat(distinct.get(4), is(9));
        assertThat(distinct.get(5), is(6));
        assertThat(distinct.get(6), is(8));
    }

    @Test
    public void shouldDistinctByEqualFunction() throws Exception {
        final FluentIterable2<People> distinct = from(peoples).distinct(new Comparator<People>() {
            @Override
            public int compare(People e, People e2) {
                return e.getId() - e2.getId();
            }
        });
        //1, 2, 3, 4, 9, 6, 3, 8, 9
        assertThat(distinct.size(), is(3));
        assertThat(distinct.get(0).getId(), is(1));
        assertThat(distinct.get(1).getId(), is(5));
        assertThat(distinct.get(2).getId(), is(9));
    }

    @Test
    public void shouldDistinctByPropertyFunction() throws Exception {
        final FluentIterable2<People> distinct = from(peoples).distinct(new Function<People, Object>() {
            @Override
            public Object apply(People input) {
                return input.getId();
            }
        });
        //1, 2, 3, 4, 9, 6, 3, 8, 9
        assertThat(distinct.size(), is(3));
        assertThat(distinct.get(0).getId(), is(1));
        assertThat(distinct.get(1).getId(), is(9));
        assertThat(distinct.get(2).getId(), is(5));
    }

    @Test
    public void shouldGetDefaultWhenFirstIsEmpty() throws Exception {
        final List<Integer> iterable = Lists.newArrayList();
        final Integer val = from(iterable).firstOrDefault(1);

        assertThat(val, is(1));

    }

    @Test
    public void shouldSaflyCast() throws Exception {
        peoples.add(new Teacher(1, "teacher 1"));

        final FluentIterable2<Teacher> teachers = from(peoples).ofType(Teacher.class);

        assertThat(teachers.size(), is(1));
        assertThat(teachers.get(0).getId(), is(1));
    }

    @Test
    public void shouldGroupBy() throws Exception {
        final List<Group<String, Integer>> group = from(dataLists).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input % 2 == 0 ? "Even" : "Odd";
            }
        }).toList();

        assertThat(group.size(), is(2));
        assertThat(group.get(0).getKey(), is("Even"));
        assertThat(group.get(0).getValues().toString(), is("[2, 4, 6, 8]"));
        assertThat(group.get(1).getKey(), is("Odd"));
        assertThat(group.get(1).getValues().toString(), is("[1, 3, 9, 3, 9]"));
    }

    @Test
    public void shouldSortById() throws Exception {
        final ImmutableList<People> list = from(peoples).orderBy(new Comparator<People>() {
            @Override
            public int compare(People people, People people2) {
                return people.getId() - people2.getId();
            }
        }).toList();

        assertThat(peoples.get(0).getId(), is(1));
        assertThat(peoples.get(1).getId(), is(9));
        assertThat(peoples.get(2).getId(), is(9));
        assertThat(peoples.get(3).getId(), is(5));

        assertThat(list.get(0).getId(), is(1));
        assertThat(list.get(1).getId(), is(5));
        assertThat(list.get(2).getId(), is(9));
        assertThat(list.get(3).getId(), is(9));
    }

    @Test
    public void shouldSortByProperty() throws Exception {
        final ImmutableList<People> list = from(peoples).orderBy(new Function<People, Object>() {
            @Override
            public Object apply(People input) {
                return input.getId();
            }
        }).toList();

        assertThat(peoples.get(0).getId(), is(1));
        assertThat(peoples.get(1).getId(), is(9));
        assertThat(peoples.get(2).getId(), is(9));
        assertThat(peoples.get(3).getId(), is(5));

        assertThat(list.get(0).getId(), is(1));
        assertThat(list.get(1).getId(), is(5));
        assertThat(list.get(2).getId(), is(9));
        assertThat(list.get(3).getId(), is(9));
    }

    @Test
    public void shouldOrderByDefault() throws Exception {
        final List<Integer> lists = from(dataLists).orderBy().toList();

        //(1, 2, 3, 4, 9, 6, 3, 8, 9);
        assertThat(dataLists.get(0), is(1));
        assertThat(dataLists.get(1), is(2));
        assertThat(dataLists.get(2), is(3));
        assertThat(dataLists.get(3), is(4));
        assertThat(dataLists.get(4), is(9));
        assertThat(dataLists.get(5), is(6));
        assertThat(dataLists.get(6), is(3));
        assertThat(dataLists.get(7), is(8));
        assertThat(dataLists.get(8), is(9));

        assertThat(lists.get(0), is(1));
        assertThat(lists.get(1), is(2));
        assertThat(lists.get(2), is(3));
        assertThat(lists.get(3), is(3));
        assertThat(lists.get(4), is(4));
        assertThat(lists.get(5), is(6));
        assertThat(lists.get(6), is(8));
        assertThat(lists.get(7), is(9));
        assertThat(lists.get(8), is(9));

    }

    @Test
    public void shouldSkipWhereGot8() throws Exception {
        final FluentIterable2<Integer> lists = from(dataLists).skipWhere(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input.equals(8);
            }
        });

        assertThat(lists.size(), is(2));
        assertThat(lists.get(0), is(8));
        assertThat(lists.get(1), is(9));
    }

    @Test
    public void shouldSumAllTheValue() throws Exception {
        final Integer sum = from(dataLists).aggregate(0, new Function2<Integer>() {
            @Override
            public Integer apply(Integer input1, Integer input2) {
                return input1 + input2;
            }
        });
        assertThat(sum, is(45));
    }

    @Test
    public void shouldSumAllTheValueByType() throws Exception {
        final Integer sum = from(dataLists).aggregate(int.class, new Function2<Integer>() {
            @Override
            public Integer apply(Integer input1, Integer input2) {
                return input1 + input2;
            }
        });
        assertThat(sum, is(45));
    }

    @Test
    public void shouldTake3() throws Exception {
        final ImmutableList<Integer> list = from(dataLists).take(3).toList();

        assertThat(list.toString(), is("[1, 2, 3]"));
    }

    @Test
    public void shouldTakeWhereMode3() throws Exception {
        final FluentIterable2<Integer> list = from(dataLists).takeWhere(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 3 == 0;
            }
        });
        list.toList();

        assertThat(list.toString(), is("[1, 2]"));
    }
}
