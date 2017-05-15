package com.commit451.reptar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Check to see if a result exists. If it does not, throw an {@link EmptyResultException}
 */
public class RequireOptionalSuccessChecker implements SuccessChecker<Optional<?>> {

    @Nullable
    @Override
    public Throwable check(@NonNull Optional<?> optional) {
        if (!optional.isPresent()) {
            return new EmptyResultException();
        }
        return null;
    }
}
