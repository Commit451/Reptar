package com.commit451.reptar;

/**
 * Represents that an empty result was found
 */
public class EmptyResultException extends Exception {

    @Override
    public String getMessage() {
        return "The result was empty";
    }
}
