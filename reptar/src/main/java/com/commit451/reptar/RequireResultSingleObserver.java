package com.commit451.reptar;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

/**
 * Subscriber for {@link Result} where we require the result to exist, otherwise we call through to
 * {@link #onError(Throwable)} with an {@link EmptyResultException}
 * @param <T> the type
 */
public abstract class RequireResultSingleObserver<T> extends ComposableSingleObserver<Result<T>>{

    /**
     * The result was a non null value
     * @param t the value
     */
    public abstract void resultSuccess(@NonNull T t);

    @CallSuper
    @Override
    public void success(@NonNull Result<T> result) {
        if (!result.isPresent()) {
            onError(new EmptyResultException());
        } else {
            resultSuccess(result.get());
        }
    }
}
