package com.commit451.reptar;

import android.support.annotation.Nullable;

/**
 * Check to see if a value should be passed on to success
 */
public interface SuccessChecker<T> {

    @Nullable
    Throwable check(T t);
}
