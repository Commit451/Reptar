package com.commit451.reptar.sample;

import java.util.concurrent.CancellationException;

import io.reactivex.Single;

/**
 * Just a bunch of observables to test with
 */
public class Observables {

    public static Single<Boolean> cancellation() {
        return Single.error(new CancellationException());
    }
}
