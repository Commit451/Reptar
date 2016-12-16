package com.commit451.reptar;

/**
 * Check to see if we should ignore a error
 */
public interface FailureChecker {

    /**
     * Perform a check to see if we should ignore this exception or not.
     * @param t the throwable
     * @return true if throwable should be ignored
     */
    boolean check(Throwable t);
}
