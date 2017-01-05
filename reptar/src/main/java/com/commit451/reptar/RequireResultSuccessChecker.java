package com.commit451.reptar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Check to see if a result exists. If it does not, throw an {@link EmptyResultException}
 */
public class RequireResultSuccessChecker implements SuccessChecker<Result<?>> {

    @Nullable
    @Override
    public Throwable check(@NonNull Result<?> result) {
        if (!result.isPresent()) {
            return new EmptyResultException();
        }
        return null;
    }
}
