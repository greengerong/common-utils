
### Checker
===========================

*   public static <T> T checkNotNull(T reference)

*   public static <T> T checkNotNull(T reference, Object message)

*   public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs)

*   public static void checkArgument(boolean expression)

*   public static void checkArgument(boolean expression, Object errorMessage)

*   public static String checkNotBlank(String reference, Object message)

*  public static String checkNotBlank(String reference) {

*  public static void checkState(boolean expression)

*   public static void checkState(boolean expression, Object errorMessage)

*   public static <T> List<T> checkHasItem(List<T> list, final T item)

*   public static <T> List<T> checkHasItem(List<T> list, final T item, Object message)

*   public static <T> List<T> checkAnyMatch(List<T> list, Predicate<? super T> predicate)

*  public static <T> List<T> checkAnyMatch(List<T> list, Predicate<? super T> predicate, Object message)

*   private static <T> boolean hasItem(List<T> list, final T item)

