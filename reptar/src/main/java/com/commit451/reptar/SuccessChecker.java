package com.commit451.reptar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Check to see if a value should be passed on to success
 */
public interface SuccessChecker<T> {

    /**
     * Perform the check to see if the value should be a success or not.
     * @param t the value
     * @return an exception that will be thrown to the observer error block, or null if the value is accepted
     */
    @Nullable
    Throwable check(@NonNull T t);
}
