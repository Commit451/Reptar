package com.commit451.reptar;

import android.support.annotation.NonNull;

import java.util.concurrent.CancellationException;

/**
 * Ignore {@link CancellationException}s
 */
public class CancellationFailureChecker implements FailureChecker {

    @Override
    public boolean check(@NonNull Throwable t) {
        return t instanceof CancellationException;
    }
}
