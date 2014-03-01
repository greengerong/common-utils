package com.github.greengerong.collection;


import com.github.greengerong.model.People;
import com.github.greengerong.model.Student;
import com.github.greengerong.model.Teacher;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.github.greengerong.collection.FluentIterable2.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Integer.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FluentIterable2Test {

    private ArrayList<Integer> dataLists;
    private ArrayList<People> peoples;

    @Before
    public void setUp() throws Exception {
        dataLists = newArrayList(1, 2, 3, 4, 9, 6, 3, 8, 9);
        peoples = new ArrayList<People>();
        peoples.add(new Student(1, "me"));
        peoples.add(new Student(9, "she"));
        peoples.add(new Student(9, "he"));
        peoples.add(new Student(5, "you"));
    }

    @Test
    public void should_select_by_condition() throws Exception {
        //given

        //when
        final List<Integer> mapLists = from(dataLists).select(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        }).toList();

        //then
        assertThat(mapLists.get(0), is(2));
        assertThat(mapLists.get(1), is(4));
        assertThat(mapLists.get(8), is(18));
    }

    @Test
    public void should_where_by_condition() throws Exception {
        //given

        //when
        final List<Integer> mapLists = from(dataLists).where(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 2 == 0;
            }
        }).toList();

        //then
        assertThat(mapLists.size(), is(4));
        assertThat(mapLists.get(0), is(2));
        assertThat(mapLists.get(1), is(4));
        assertThat(mapLists.get(2), is(6));
        assertThat(mapLists.get(3), is(8));
    }

    @Test
    public void should_cast_all_element() throws Exception {
        //given

        //when
        final FluentIterable2<Student> students = from(peoples).cast();

        //then
        assertThat(students.size(), is(4));
    }

    @Test
    public void should_for_each() throws Exception {
        //given

        //when
        final List<Integer> lists = newArrayList();
        from(dataLists).forEach(new Action<Integer>() {
            @Override
            public void apply(Integer input) {
                if (input > 8) {
                    lists.add(input);
                }
            }
        });

        //then
        assertThat(lists.size(), is(2));
        assertThat(lists.get(0), is(9));
        assertThat(lists.get(1), is(9));
    }

    @Test
    public void should_to_map() throws Exception {
        //given

        //when
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

        //then
        assertThat(map.get(1), is("me"));
        assertThat(map.get(5), is("you"));
    }

    @Test
    public void should_distinct() throws Exception {
        //given

        //when
        final FluentIterable2<Integer> distinct = from(dataLists).distinct();
        //1, 2, 3, 4, 9, 6, 3, 8, 9

        //then
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
    public void should_distinct_by_equal_function() throws Exception {
        //given

        //condition
        final FluentIterable2<People> distinct = from(peoples).distinct(new Comparator<People>() {
            @Override
            public int compare(People e, People e2) {
                return e.getId() - e2.getId();
            }
        });
        //1, 2, 3, 4, 9, 6, 3, 8, 9

        //then
        assertThat(distinct.size(), is(3));
        assertThat(distinct.get(0).getId(), is(1));
        assertThat(distinct.get(1).getId(), is(5));
        assertThat(distinct.get(2).getId(), is(9));
    }

    @Test
    public void should_distinct_by_property_function() throws Exception {
        //given

        //when
        final FluentIterable2<People> distinct = from(peoples).distinct(new Function<People, Object>() {
            @Override
            public Object apply(People input) {
                return input.getId();
            }
        });
        //1, 2, 3, 4, 9, 6, 3, 8, 9

        //then
        assertThat(distinct.size(), is(3));
        assertThat(distinct.get(0).getId(), is(1));
        assertThat(distinct.get(1).getId(), is(9));
        assertThat(distinct.get(2).getId(), is(5));
    }

    @Test
    public void should_get_default_when_first_is_empty() throws Exception {
        //given

        //when
        final List<Integer> iterable = newArrayList();
        final Integer val = from(iterable).firstOrDefault(1);

        //then
        assertThat(val, is(1));

    }

    @Test
    public void should_safly_cast() throws Exception {
        //given

        peoples.add(new Teacher(1, "teacher 1"));

        //when
        final FluentIterable2<Teacher> teachers = from(peoples).ofType(Teacher.class);

        //then
        assertThat(teachers.size(), is(1));
        assertThat(teachers.get(0).getId(), is(1));
    }

    @Test
    public void should_group_by() throws Exception {
        //given

        //when
        final List<Group<String, Integer>> group = from(dataLists).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input % 2 == 0 ? "Even" : "Odd";
            }
        }).toList();

        //then
        assertThat(group.size(), is(2));
        assertThat(group.get(0).getKey(), is("Even"));
        assertThat(group.get(0).getValues().toString(), is("[2, 4, 6, 8]"));
        assertThat(group.get(1).getKey(), is("Odd"));
        assertThat(group.get(1).getValues().toString(), is("[1, 3, 9, 3, 9]"));
    }

    @Test
    public void should_sort_by_id() throws Exception {
        //given

        //when
        final ImmutableList<People> list = from(peoples).orderBy(new Comparator<People>() {
            @Override
            public int compare(People people, People people2) {
                return people.getId() - people2.getId();
            }
        }).toList();

        //then
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
    public void should_sort_by_property() throws Exception {
        //given

        //when
        final ImmutableList<People> list = from(peoples).orderBy(new Function<People, Object>() {
            @Override
            public Object apply(People input) {
                return input.getId();
            }
        }).toList();

        //then
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
    public void should_order_by_default() throws Exception {
        //given

        //when
        final List<Integer> lists = from(dataLists).orderBy().toList();

        //(1, 2, 3, 4, 9, 6, 3, 8, 9);

        //then
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
    public void should_skip_where_get_8() throws Exception {
        //given

        //when
        final FluentIterable2<Integer> lists = from(dataLists).skipWhere(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input.equals(8);
            }
        });

        //then
        assertThat(lists.size(), is(2));
        assertThat(lists.get(0), is(8));
        assertThat(lists.get(1), is(9));
    }

    @Test
    public void should_sum_all_the_value() throws Exception {
        final Integer sum = from(dataLists).aggregate(0, new AggregateFunction<Integer, Integer>() {
            @Override
            public Integer apply(Integer input1, Integer input2) {
                return input1 + input2;
            }
        });

        //then
        assertThat(sum, is(45));
    }

    @Test
    public void should_sum_all_the_value_by_type() throws Exception {
        //given

        //when
        final Integer sum = from(dataLists).aggregate(int.class, new AggregateFunction<Integer, Integer>() {
            @Override
            public Integer apply(Integer input1, Integer input2) {
                return input1 + input2;
            }
        });

        //then
        assertThat(sum, is(45));
    }

    @Test
    public void should_take_3() throws Exception {
        final ImmutableList<Integer> list = from(dataLists).take(3).toList();

        //then
        assertThat(list.toString(), is("[1, 2, 3]"));
    }

    @Test
    public void should_take_where_mode_3() throws Exception {
        //given

        //when
        final FluentIterable2<Integer> list = from(dataLists).takeWhere(new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input % 3 == 0;
            }
        });
        list.toList();

        //then
        assertThat(list.toString(), is("[1, 2]"));
    }

    @Test
    public void should_zip() throws Exception {
        //given

        //when
        final List<Integer> first = newArrayList(1, 2, 3);
        final List<Integer> second = newArrayList(1, 2);
        final ImmutableList<Integer> list = from(first).zip(second, new ZipFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer input1, Integer input2) {
                return input1 * input2;
            }
        }).toList();

        //then
        assertThat(list.toString(), is("[1, 4]"));
    }

    @Test
    public void should_flatten_list() throws Exception {
        //given

        //when
        final List<Serializable> first = newArrayList(valueOf(1), valueOf(2), newArrayList(valueOf(1), valueOf(2), valueOf(3), newArrayList(8, 9, newArrayList(10, 9))));
        final ImmutableList<Serializable> list = from(first).flatten().toList();

        //then
        assertThat(list.toString(), is("[1, 2, 1, 2, 3, 8, 9, 10, 9]"));
    }
}
