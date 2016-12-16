package com.commit451.reptar;

import android.support.annotation.CallSuper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * {@link SingleObserver} which does not care about the {@link #onSubscribe(Disposable)}
 *
 * @param <T> the type
 */
public abstract class ComposableSingleObserver<T> implements SingleObserver<T> {

    private List<SuccessChecker> successCheckers;
    private List<FailureChecker> failureCheckers;

    public abstract void success(T t);

    public abstract void error(Throwable t);

    @CallSuper
    @Override
    public void onSuccess(T value) {
        if (successCheckers != null) {
            for (SuccessChecker successChecker : successCheckers) {
                Throwable throwable = successChecker.check(value);
                if (throwable != null) {
                    error(throwable);
                    return;
                }
            }
        }
        success(value);
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
    }

    public ComposableSingleObserver add(SuccessChecker successChecker) {
        if (successCheckers == null) {
            successCheckers = new ArrayList<>();
        }
        successCheckers.add(successChecker);
        return this;
    }

    public ComposableSingleObserver add(FailureChecker failureChecker) {
        if (failureCheckers == null) {
            failureCheckers = new ArrayList<>();
        }
        failureCheckers.add(failureChecker);
        return this;
    }
}
