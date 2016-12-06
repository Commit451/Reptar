package com.commit451.reptar;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * {@link io.reactivex.SingleObserver} that does not care about {@link io.reactivex.SingleObserver#onSubscribe(Disposable)}
 */
public abstract class SimpleSingleObserver<T> implements SingleObserver<T> {

    @Override
    public void onSubscribe(Disposable d) {
        //cool!
    }
}
