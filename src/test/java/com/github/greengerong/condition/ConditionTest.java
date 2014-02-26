package com.github.greengerong.condition;


import com.github.greengerong.condition.expression.WhenFunctionExpression;
import com.github.greengerong.model.Sex;
import org.junit.Test;

import java.util.List;

import static com.github.greengerong.condition.Condition.when;
import static com.github.greengerong.condition.expression.WhenPredicateExpression.*;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConditionTest {

    @Test
    public void should_handle_by_first_matched_hanlder() throws Exception {
        //given

        //when
        final Sex sex = when()
                .then(eq(1), WhenFunctionExpression.returnWith(Sex.MALE))
                .then(eq(2), WhenFunctionExpression.returnWith(Sex.FEMALE))
                .single(1);


        //then
        assertThat(sex, is(Sex.MALE));
    }

    @Test
    public void should_return_null_when_no_matched_hanlder() throws Exception {
        //given

        //when
        final Sex sex = when()
                .then(eq(2), WhenFunctionExpression.returnWith(Sex.FEMALE))
                .single(1);

        //then
        assertThat(sex, is(nullValue()));
    }

    @Test
    public void should_handle_by_all_matched_hanlder() throws Exception {
        //given

        //when
        final List<Sex> sexes = when()
                .then(eq(1), WhenFunctionExpression.returnWith(Sex.MALE))
                .then(eq(2), WhenFunctionExpression.returnWith(Sex.FEMALE))
                .then(anything(), WhenFunctionExpression.returnWith(Sex.MALE))
                .all(1);

        //then
        assertThat(sexes.size(), is(2));
        assertThat(sexes.get(0), is(Sex.MALE));
        assertThat(sexes.get(1), is(Sex.MALE));
    }

    @Test
    public void should_handle_by_first_hanlder_not_otherwise() throws Exception {
        //given

        //when
        final List<Sex> sexes = when()
                .then(eq(1), WhenFunctionExpression.returnWith(Sex.MALE))
                .then(eq(2), WhenFunctionExpression.returnWith(Sex.FEMALE))
                .otherwise(WhenFunctionExpression.returnWith(Sex.MALE))
                .all(2);

        //then
        assertThat(sexes.size(), is(1));
        assertThat(sexes.get(0), is(Sex.FEMALE));
    }

    @Test
    public void should_handle_by_otherwise_for_single() throws Exception {
        //given

        //when
        final Sex sex = when()
                .then(eq(1), WhenFunctionExpression.returnWith(Sex.MALE))
                .then(eq(2), WhenFunctionExpression.returnWith(Sex.MALE))
                .otherwise(WhenFunctionExpression.returnWith(Sex.FEMALE))
                .single(3);

        //then
        assertThat(sex, is(Sex.FEMALE));
    }

    @Test
    public void should_handle_by_otherwise_for_all() throws Exception {
        //given

        //when
        final List<Sex> sexes = when()
                .then(eq(1), WhenFunctionExpression.returnWith(Sex.MALE))
                .then(eq(2), WhenFunctionExpression.returnWith(Sex.MALE))
                .otherwise(WhenFunctionExpression.returnWith(Sex.FEMALE))
                .all(3);

        //then
        assertThat(sexes.size(), is(1));
        assertThat(sexes.get(0), is(Sex.FEMALE));
    }
}
