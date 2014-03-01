
### Condition
===========================

### condition

* final T first = condition().when(equalTo(xx), returnWith(xx)).when(equalTo(xx), returnWith(xx)).first(seed);

* final List<T> pipe = condition().when(equalTo(xx), returnWith(xx)).when(equalTo(xx), returnWith(xx)).pipe(seed);

* final T first = condition().when(equalTo(xx), returnWith(xx)).when(equalTo(xx), returnWith(xx)).otherwise(xx).first(seed);

* final List<T> pipe = condition().when(equalTo(xx), returnWith(xx)).when(equalTo(xx), returnWith(xx)).otherwise(xx).pipe(seed);