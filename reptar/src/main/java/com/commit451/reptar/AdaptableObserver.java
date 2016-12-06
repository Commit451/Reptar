package com.commit451.reptar;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * {@link Observer} with methods implemented, allowing you to choose which to implement
 */
public class AdaptableObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onNext(T value) {
    }

    @Override
    public void onError(Throwable e) {
    }
}
