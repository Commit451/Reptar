package com.commit451.reptar;

public interface FailureChecker {

    /**
     *
     * @param t
     * @return true if throwable should be ignored
     */
    boolean check(Throwable t);
}
