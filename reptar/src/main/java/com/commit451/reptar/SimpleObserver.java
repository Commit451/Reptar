package com.commit451.reptar;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * {@link SingleObserver} which does not care about the {@link #onSubscribe(Disposable)}
 * or {@link #onComplete()}
 * @param <T>
 */
public abstract class SimpleObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {
    }
}
