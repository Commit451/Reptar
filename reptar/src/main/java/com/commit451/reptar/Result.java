package com.commit451.reptar;

import android.support.annotation.Nullable;

/**
 * Since {@code null} is not a valid result to emit, there may still be times when we want to pass
 * either a value, or nothing (such as if we are checking if the user is in a datastore). This class
 * allows you to pass a result, or null, if there is no result.
 * @see <a href=https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#nulls>https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#nulls</a>
 */
public class Result<T> {

    private T value;

    /**
     * Construct a result, which can be emitted
     * @param value the value, or null to give a null result
     */
    public Result(@Nullable T value) {
        this.value = value;
    }

    /**
     * Check to see if a value exists
     * @return true if a value exists, false if not
     */
    public boolean hasValue() {
        return value != null;
    }

    /**
     * Get the value within the result. Typically, you should check {@link #hasValue()} first
     * @return the value, or null if there is none
     */
    public T value() {
        return value;
    }
}
