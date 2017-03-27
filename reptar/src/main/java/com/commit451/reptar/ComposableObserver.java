package com.commit451.reptar;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * {@link Observer} which can be composed with {@link SuccessChecker}s and {@link FailureChecker}s.
 *
 * @param <T> the type
 */
public abstract class ComposableObserver<T> implements Observer<T> {

    private List<SuccessChecker> successCheckers;
    private List<FailureChecker> failureCheckers;
    private Disposable disposable;

    public abstract void next(@NonNull T t);

    public abstract void error(@NonNull Throwable t);

    @CallSuper
    @Override
    public void onNext(T value) {
        if (successCheckers != null) {
            for (SuccessChecker successChecker : successCheckers) {
                Throwable throwable = successChecker.check(value);
                if (throwable != null) {
                    error(throwable);
                    return;
                }
            }
        }
        next(value);
    }

    @CallSuper
    @Override
    public void onError(Throwable e) {
        if (failureCheckers != null) {
            for (FailureChecker failureChecker : failureCheckers) {
                boolean result = failureChecker.check(e);
                if (result) {
                    return;
                }
            }
        }
        //we made it through all the checks, so now we can alert
        error(e);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onComplete() {
    }

    public ComposableObserver<T> add(SuccessChecker successChecker) {
        if (successCheckers == null) {
            successCheckers = new ArrayList<>();
        }
        successCheckers.add(successChecker);
        return this;
    }

    public ComposableObserver<T> add(FailureChecker failureChecker) {
        if (failureCheckers == null) {
            failureCheckers = new ArrayList<>();
        }
        failureCheckers.add(failureChecker);
        return this;
    }

    public Disposable disposable() {
        return disposable;
    }
}
