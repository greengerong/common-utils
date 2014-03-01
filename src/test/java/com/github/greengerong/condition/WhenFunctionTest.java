package com.github.greengerong.condition;


import com.github.greengerong.model.Sex;
import org.junit.Test;

import java.util.List;

import static com.github.greengerong.condition.ConditionFactory.condition;
import static com.github.greengerong.condition.expression.WhenFunctionExpression.returnWith;
import static com.github.greengerong.condition.expression.WhenPredicateExpression.anything;
import static com.google.common.base.Predicates.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WhenFunctionTest {

    @Test
    public void should_handle_by_first_matched_hanlder() throws Exception {
        //given

        //condition
        final Sex sex = condition()
                .when(equalTo(1), returnWith(Sex.MALE))
                .when(equalTo(2), returnWith(Sex.FEMALE))
                .single(1);


        //when
        assertThat(sex, is(Sex.MALE));
    }

    @Test
    public void should_return_null_when_no_matched_hanlder() throws Exception {
        //given

        //condition
        final Sex sex = condition()
                .when(equalTo(2), returnWith(Sex.FEMALE))
                .single(1);

        //when
        assertThat(sex, is(nullValue()));
    }

    @Test
    public void should_handle_by_all_matched_hanlder() throws Exception {
        //given

        //condition
        final List<Sex> sexes = condition()
                .when(equalTo(1), returnWith(Sex.MALE))
                .when(equalTo(2), returnWith(Sex.FEMALE))
                .when(anything(), returnWith(Sex.MALE))
                .all(1);

        //when
        assertThat(sexes.size(), is(2));
        assertThat(sexes.get(0), is(Sex.MALE));
        assertThat(sexes.get(1), is(Sex.MALE));
    }

    @Test
    public void should_handle_by_first_hanlder_not_otherwise() throws Exception {
        //given

        //condition
        final List<Sex> sexes = condition()
                .when(equalTo(1), returnWith(Sex.MALE))
                .when(equalTo(2), returnWith(Sex.FEMALE))
                .otherwise(returnWith(Sex.MALE))
                .pipe(2);

        //when
        assertThat(sexes.size(), is(1));
        assertThat(sexes.get(0), is(Sex.FEMALE));
    }

    @Test
    public void should_handle_by_otherwise_for_single() throws Exception {
        //given

        //condition
        final Sex sex = condition()
                .when(equalTo(1), returnWith(Sex.MALE))
                .when(equalTo(2), returnWith(Sex.MALE))
                .otherwise(returnWith(Sex.FEMALE))
                .first(3);

        //when
        assertThat(sex, is(Sex.FEMALE));
    }

    @Test
    public void should_handle_by_otherwise_for_all() throws Exception {
        //given

        //condition
        final List<Sex> sexes = condition()
                .when(equalTo(1), returnWith(Sex.MALE))
                .when(equalTo(2), returnWith(Sex.MALE))
                .otherwise(returnWith(Sex.FEMALE))
                .pipe(3);

        //when
        assertThat(sexes.size(), is(1));
        assertThat(sexes.get(0), is(Sex.FEMALE));
    }
}
