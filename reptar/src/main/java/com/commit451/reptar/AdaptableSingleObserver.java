package com.commit451.reptar;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * {@link SingleObserver} with methods implemented, allowing you to choose which to implement
 * @param <T> the type
 */
public class AdaptableSingleObserver<T> implements SingleObserver<T> {

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onSuccess(T value) {
    }
}
