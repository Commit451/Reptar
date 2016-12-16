package com.commit451.reptar;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

/**
 * Created by Jawn on 12/15/2016.
 */

public abstract class RequireResultSingleObserver<T> extends ComposableSingleObserver<Result<T>>{

    /**
     * The result was a non null value
     * @param t the value
     */
    public abstract void resultSuccess(@NonNull T t);

    @CallSuper
    @Override
    public void success(Result<T> result) {
        if (!result.isPresent()) {
            onError(new EmptyResultException());
        } else {
            resultSuccess(result.get());
        }
    }
}
