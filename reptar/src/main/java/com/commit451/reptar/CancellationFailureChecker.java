package com.commit451.reptar;

import java.util.concurrent.CancellationException;

/**
 * Ignore {@link CancellationException}s
 */
public class CancellationFailureChecker implements FailureChecker {

    @Override
    public boolean check(Throwable t) {
        return !(t instanceof CancellationException);
    }
}
