package com.commit451.reptar;

import android.support.annotation.CallSuper;

import java.util.concurrent.CancellationException;

/**
 * {@link FocusedSingleObserver} which ignores {@link java.util.concurrent.CancellationException}s.
 * This is useful for when you are using methods that cancel subscriptions, but you do not want to execute
 * the error block when it is a cancellation due to the fact that your cancellation is most likely an
 * indicator that the UI no longer exists, such as if you use RxLifecycle and are in a fragment and
 * do not want to get crashes relating to the UI no longer existing in the onError block
 * @see <a href="https://github.com/trello/RxLifecycle">https://github.com/trello/RxLifecycle</a>
 */
public abstract class IgnoreCancellationSingleObserver<T> extends FocusedSingleObserver<T> {

    /**
     * An error has occurred which is not a {@link CancellationException}, which are swallowed
     * @param e the throwable
     */
    public abstract void onNonCancellationError(Throwable e);

    @CallSuper
    @Override
    public void onError(Throwable e) {
        if (!(e instanceof CancellationException)) {
            onNonCancellationError(e);
        }
    }
}
