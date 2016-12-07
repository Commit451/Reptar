package com.commit451.reptar;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * {@link SingleObserver} which does not care about the {@link #onSubscribe(Disposable)}
 * @param <T> the type
 */
public abstract class FocusedSingleObserver<T> implements SingleObserver<T> {

    @Override
    public void onSubscribe(Disposable d) {
    }
}
