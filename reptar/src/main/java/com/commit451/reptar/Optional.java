package com.commit451.reptar;

import android.support.annotation.Nullable;

/**
 * Since {@code null} is not a valid result to emit, there may still be times when we want to pass
 * either a get, or nothing (such as if we are checking if the user is in a datastore). This class
 * allows you to pass a result, or null, if there is no result. Similar to Optional in Guava
 * @see <a href=https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#nulls>https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#nulls</a>
 * @see <a href=https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained>https://github.com/google/guava/wiki/UsingAndAvoidingNullExplained</a>
 */
public class Optional<T> {

    private static final Optional EMPTY = new Optional();

    /**
     * If you have no result, gives you an empty result, which avoids object allocation
     */
    public static <T> Optional<T> empty() {
        return EMPTY;
    }

    private T value;

    /**
     * Construct an empty result, which can be emitted
     * @see #EMPTY
     */
    public Optional() {
    }

    /**
     * Construct a result, which can be emitted
     * @param value the value, or null to give a null result
     */
    public Optional(@Nullable T value) {
        this.value = value;
    }

    /**
     * Check to see if a result exists
     * @return true if a result exists, false if not
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Get the get within the result. Typically, you should check {@link #isPresent()} first
     * @return the value, or null if there is none
     */
    public T get() {
        return value;
    }
}
