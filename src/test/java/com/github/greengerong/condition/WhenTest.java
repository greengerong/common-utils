package com.github.greengerong.condition;


import com.github.greengerong.model.People;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WhenTest {

    @Test
    public void should_handle_by_first_matched_hanlder() throws Exception {
        People people = new People(1, "me");
        final String name = new When()
                .then(getOddPredicate(), getOddFunction())
                .then(getEvenPredicate(), getEvenFunction())
                .done(people, String.class);


        assertThat(name, is("me-me"));

    }

    @Test
    public void should_return_null_when_no_matched_hanlder() throws Exception {
        People people = new People(1, "me");
        final String name = new When()
                .then(getEvenPredicate(), getEvenFunction())
                .done(people, String.class);


        assertThat(name, is(nullValue()));

    }

    @Test
    public void should_handle_by_all_matched_hanlder() throws Exception {
        People people = new People(1, "me");
        final List<String> name = new When()
                .then(getOddPredicate(), getOddFunction())
                .then(getDefaultPredicate(), getDefaultFunction())
                .done(people, String.class, false);


        assertThat(name.get(0), is("me-me"));
        assertThat(name.get(1), is("me-me-default"));

    }

    @Test
    public void should_handle_by_first_matched_hanlder_with_first_parameter_true() throws Exception {
        People people = new People(1, "me");
        final List<String> name = new When()
                .then(getOddPredicate(), getOddFunction())
                .then(getDefaultPredicate(), getDefaultFunction())
                .done(people, String.class, true);


        assertThat(name.get(0), is("me-me"));

    }

    public Predicate<People> getDefaultPredicate() {
        return new Predicate<People>() {
            @Override
            public boolean apply(People input) {
                return true;
            }
        };

    }

    private Function<People, String> getDefaultFunction() {
        return new Function<People, String>() {
            @Override
            public String apply(People input) {
                return String.format("%s-%s-default", input.getName(), input.getName());
            }
        };
    }

    private Function<People, String> getOddFunction() {
        return new Function<People, String>() {
            @Override
            public String apply(People input) {
                return String.format("%s-%s", input.getName(), input.getName());
            }
        };
    }

    private Predicate<People> getOddPredicate() {
        return new Predicate<People>() {
            @Override
            public boolean apply(People input) {
                return input.getId() % 2 != 0;
            }
        };
    }

    private Function<People, String> getEvenFunction() {
        return new Function<People, String>() {
            @Override
            public String apply(People input) {
                return input.getName();
            }
        };
    }

    private Predicate<People> getEvenPredicate() {
        return new Predicate<People>() {
            @Override
            public boolean apply(People input) {
                return input.getId() % 2 == 0;
            }
        };
    }


}
