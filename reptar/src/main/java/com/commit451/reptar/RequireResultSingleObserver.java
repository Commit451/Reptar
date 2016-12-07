package com.commit451.reptar;

/**
 * Subscriber for {@link Result} where we require the result to exist, otherwise we call through to
 * {@link #onError(Throwable)} with an {@link EmptyResultException}
 */
public abstract class RequireResultSingleObserver<T> extends FocusedSingleObserver<Result<T>> {

    public abstract void onResultSuccess(T t);

    @Override
    public void onSuccess(Result<T> result) {
        if (!result.hasValue()) {
            onError(new EmptyResultException());
        } else {
            onResultSuccess(result.value());
        }
    }
}
