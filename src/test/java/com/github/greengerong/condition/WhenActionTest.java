package com.github.greengerong.condition;


import com.github.greengerong.collection.Action;
import com.google.common.base.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.github.greengerong.condition.WhenFactory.when;
import static com.google.common.base.Predicates.alwaysTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WhenActionTest {

    @Mock
    private StubCacheService mockStubCacheService;

    @Test
    public void should_handle_by_first_matched_hanlder() throws Exception {
        //given
        final StuCache model = new StuCache(Status.NEW);

        //when
        when()
                .then(outDate(), removeEntity())
                .then(newOne(), addEntiry())
                .single(model);


        //then
        verify(mockStubCacheService).add(model);
        verify(mockStubCacheService, never()).remove(model);
        verify(mockStubCacheService, never()).log(model);
    }

    @Test
    public void should_nothing_to_do_when_no_matched_hanlder() throws Exception {
        //given
        final StuCache model = new StuCache(Status.OUT_DATE);

        //when
        when()
                .then(newOne(), addEntiry())
                .single(model);

        //then
        verify(mockStubCacheService, never()).add(model);
        verify(mockStubCacheService, never()).remove(model);
        verify(mockStubCacheService, never()).log(model);
    }

    @Test
    public void should_handle_by_all_matched_hanlder() throws Exception {
        //given
        StuCache model = new StuCache(Status.OUT_DATE);

        //when
        when()
                .then(newOne(), addEntiry())
                .then(outDate(), removeEntity())
                .then(log(), logEntity())
                .all(model);

        //then
        verify(mockStubCacheService, never()).add(model);
        verify(mockStubCacheService).remove(model);
        verify(mockStubCacheService).log(model);
    }

    @Test
    public void should_handle_by_first_hanlder_not_otherwise() throws Exception {
        //given
        StuCache model = new StuCache(Status.OUT_DATE);

        //when
        when()
                .then(newOne(), addEntiry())
                .then(outDate(), removeEntity())
                .otherwise(logEntity())
                .all(model);

        //then
        verify(mockStubCacheService, never()).add(model);
        verify(mockStubCacheService).remove(model);
        verify(mockStubCacheService, never()).log(model);
    }

    @Test
    public void should_handle_by_otherwise_for_single() throws Exception {
        //given
        StuCache model = new StuCache(Status.OUT_DATE);

        //when
        when()
                .then(newOne(), addEntiry())
                .otherwise(logEntity())
                .single(model);

        //then
        verify(mockStubCacheService, never()).add(model);
        verify(mockStubCacheService, never()).remove(model);
        verify(mockStubCacheService).log(model);
    }

    @Test
    public void should_handle_by_otherwise_for_all() throws Exception {
        //given
        StuCache model = new StuCache(Status.OUT_DATE);

        //when
        when()
                .then(newOne(), addEntiry())
                .otherwise(logEntity())
                .all(model);

        //then
        verify(mockStubCacheService, never()).add(model);
        verify(mockStubCacheService, never()).remove(model);
        verify(mockStubCacheService).log(model);
    }

    private Action<StuCache> removeEntity() {
        return new Action<StuCache>() {
            @Override
            public void apply(StuCache input) {
                mockStubCacheService.remove(input);
            }
        };
    }

    private Predicate<StuCache> outDate() {
        return getStuModelPredicate(Status.OUT_DATE);
    }

    private Action<StuCache> addEntiry() {
        return new Action<StuCache>() {
            @Override
            public void apply(StuCache input) {
                mockStubCacheService.add(input);
            }
        };
    }


    private Predicate<StuCache> newOne() {
        return getStuModelPredicate(Status.NEW);
    }

    private Predicate<StuCache> getStuModelPredicate(final Status status) {
        return new Predicate<StuCache>() {

            @Override
            public boolean apply(StuCache input) {
                return input.getStatus() == status;
            }
        };
    }

    private Predicate log() {
        return alwaysTrue();
    }


    private Action<StuCache> logEntity() {
        return new Action<StuCache>() {
            @Override
            public void apply(StuCache input) {
                mockStubCacheService.log(input);
            }
        };
    }

    interface StubCacheService {

        void add(StuCache o);

        void remove(StuCache o);

        void log(StuCache input);
    }

    class StuCache {

        private Status status;

        StuCache(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }


    }

    enum Status {
        NEW, OUT_DATE
    }
}
