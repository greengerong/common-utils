package com.github.greengerong.condition;


import com.github.greengerong.model.Sex;
import org.junit.Test;

import java.util.List;

import static com.github.greengerong.condition.expression.WhenExpression.*;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WhenTest {

    @Test
    public void should_handle_by_first_matched_hanlder() throws Exception {
        final Sex sex = new When()
                .then(eq(1), returnWith(Sex.MALE))
                .then(eq(2), returnWith(Sex.FEMALE))
                .single(1);


        assertThat(sex, is(Sex.MALE));

    }

    @Test
    public void should_return_null_when_no_matched_hanlder() throws Exception {
        final Sex sex = new When()
                .then(eq(2), returnWith(Sex.FEMALE))
                .single(1);

        assertThat(sex, is(nullValue()));
    }

    @Test
    public void should_handle_by_all_matched_hanlder() throws Exception {
        final List<Sex> sexes = new When()
                .then(eq(1), returnWith(Sex.MALE))
                .then(eq(2), returnWith(Sex.FEMALE))
                .then(anything(), returnWith(Sex.MALE))
                .all(1);

        assertThat(sexes.size(), is(2));
        assertThat(sexes.get(0), is(Sex.MALE));
        assertThat(sexes.get(1), is(Sex.MALE));

    }

    @Test
    public void should_handle_by_first_hanlder_not_otherwise() throws Exception {
        final List<Sex> sexes = new When()
                .then(eq(1), returnWith(Sex.MALE))
                .then(eq(2), returnWith(Sex.FEMALE))
                .otherwise(returnWith(Sex.MALE))
                .all(2);

        assertThat(sexes.size(), is(1));
        assertThat(sexes.get(0), is(Sex.FEMALE));
    }

    @Test
    public void should_handle_by_otherwise_for_single() throws Exception {
        final Sex sex = new When()
                .then(eq(1), returnWith(Sex.MALE))
                .then(eq(2), returnWith(Sex.MALE))
                .otherwise(returnWith(Sex.FEMALE))
                .single(3);

        assertThat(sex, is(Sex.FEMALE));
    }

    @Test
    public void should_handle_by_otherwise_for_all() throws Exception {
        final List<Sex> sexes = new When()
                .then(eq(1), returnWith(Sex.MALE))
                .then(eq(2), returnWith(Sex.MALE))
                .otherwise(returnWith(Sex.FEMALE))
                .all(3);

        assertThat(sexes.size(), is(1));
        assertThat(sexes.get(0), is(Sex.FEMALE));
    }
}
