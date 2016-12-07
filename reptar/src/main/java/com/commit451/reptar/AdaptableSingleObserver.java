package com.commit451.reptar;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * {@link SingleObserver} that does not care about {@link io.reactivex.SingleObserver#onSubscribe(Disposable)}
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
