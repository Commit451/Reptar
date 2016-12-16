package com.commit451.reptar.sample;

import com.commit451.reptar.CancellationFailureChecker;
import com.commit451.reptar.ComposableSingleObserver;

/**
 * Example of creating a custom single observer to hold duplicating logical setup
 */
public abstract class CustomSingleObserver<T> extends ComposableSingleObserver<T> {

    public CustomSingleObserver() {
        add(new CancellationFailureChecker());
    }

}
