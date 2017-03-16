package com.commit451.reptar.retrofit;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * {@link Function} that automatically throws {@link HttpException} if the response
 * is a non-200 response code. Useful for flatMap functions
 */
public abstract class ResponseFunction<T, R> implements Function<Response<T>, R> {

    /**
     * Called when the response is a success (2XX response code)
     * @param t the response body
     * @return the output value
     * @throws Exception on error
     */
    protected abstract R applyResponse(@Nullable T t) throws Exception;

    @CallSuper
    @Override
    public R apply(@NonNull Response<T> tResponse) throws Exception {
        if (!tResponse.isSuccessful()) {
            throw new HttpException(tResponse);
        }
        return applyResponse(tResponse.body());
    }
}
