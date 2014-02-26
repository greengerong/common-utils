
### Checker
===========================

* final T single = when().then(eq(xx), returnWith(xx)).then(eq(xx), returnWith(xx)).single(seed);

* final List<T> all = when().then(eq(xx), returnWith(xx)).then(eq(xx), returnWith(xx)).all(seed);

* final T single = when().then(eq(xx), returnWith(xx)).then(eq(xx), returnWith(xx)).otherwise(xx).single(seed);

* final List<T> all = when().then(eq(xx), returnWith(xx)).then(eq(xx), returnWith(xx)).otherwise(xx).all(seed);