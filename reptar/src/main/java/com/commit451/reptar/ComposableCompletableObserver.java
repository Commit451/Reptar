package com.commit451.reptar;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

/**
 * {@link CompletableObserver} which can be composed with {@link SuccessChecker}s and {@link FailureChecker}s.
 */
public abstract class ComposableCompletableObserver implements CompletableObserver {

    private List<FailureChecker> failureCheckers;
    private Disposable disposable;

    public abstract void complete();

    public abstract void error(@NonNull Throwable t);

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

    @CallSuper
    @Override
    public void onComplete() {
        //in case we ever want to override some other way
        complete();
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    public ComposableCompletableObserver add(FailureChecker failureChecker) {
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
