package com.github.greengerong.checker;


import com.google.common.base.Predicate;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.List;

import static com.google.common.collect.FluentIterable.from;
import static org.apache.commons.lang.StringUtils.isBlank;

public class Assert {

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new CheckerRuntimeException(new NullPointerException());
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, Object message) {
        if (reference == null) {
            throw new CheckerRuntimeException(String.valueOf(message));
        }
        return reference;
    }


    public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
        if (reference == null) {
            final NullPointerException ex = new NullPointerException(MessageFormat.format(errorMessageTemplate, errorMessageArgs));
            throw new CheckerRuntimeException(ex);
        }
        return reference;
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new CheckerRuntimeException(new IllegalArgumentException());
        }
    }

    public static void checkArgument(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new CheckerRuntimeException(new IllegalArgumentException(String.valueOf(errorMessage)));
        }
    }


    public static String checkNotBlank(String reference, Object message) {
        if (isBlank(reference)) {
            throw new CheckerRuntimeException(String.valueOf(message));
        }

        return reference;
    }

    public static String checkNotBlank(String reference) {
        if (isBlank(reference)) {
            throw new CheckerRuntimeException();
        }

        return reference;
    }

    public static String checkFileExists(String reference) {
        return checkFileExists(reference, "File {0} not found.");
    }

    public static String checkFileExists(File reference) {
        return checkFileExists(checkNotNull(reference), "File {0} not found.");
    }

    public static String checkFileExists(File reference, String message) {
        return checkFileExists(checkNotNull(reference).getAbsolutePath(), message);
    }

    public static String checkFileExists(String reference, String message) {
        if (!FileUtils.fileExists(checkNotBlank(reference))) {
            throw new CheckerRuntimeException(new FileNotFoundException(MessageFormat.format(message, reference)));
        }
        return reference;
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new CheckerRuntimeException(new IllegalStateException());
        }
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new CheckerRuntimeException(new IllegalStateException(String.valueOf(errorMessage)));
        }
    }

    public static <T> List<T> checkHasItem(List<T> list, final T item) {
        if (!hasItem(list, item)) {
            throw new CheckerRuntimeException();
        }

        return list;
    }

    public static <T> List<T> checkHasItem(List<T> list, final T item, Object message) {
        if (!hasItem(list, item)) {
            throw new CheckerRuntimeException(String.valueOf(message));
        }

        return list;
    }

    public static <T> List<T> checkAnyMatch(List<T> list, Predicate<? super T> predicate) {
        if (!from(list).anyMatch(predicate)) {
            throw new CheckerRuntimeException();
        }

        return list;
    }

    public static <T> List<T> checkAnyMatch(List<T> list, Predicate<? super T> predicate, Object message) {
        if (!from(list).anyMatch(predicate)) {
            throw new CheckerRuntimeException(String.valueOf(message));
        }

        return list;
    }

    private static <T> boolean hasItem(List<T> list, final T item) {
        return from(list).anyMatch(new Predicate<T>() {
            @Override
            public boolean apply(T input) {
                return input.equals(item);
            }
        });
    }
}
