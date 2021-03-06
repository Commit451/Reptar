package com.commit451.reptar.retrofit;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.commit451.reptar.ComposableSingleObserver;

import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Subscriber that sends HTTP error codes into the {@link #onError(Throwable)}
 * block, but also allows access to the Retrofit response. This is useful
 * for cases where you do not need or want to still check {@link Response#isSuccessful()}
 * but also would like to be able to access the Retrofit response in your success block (via {@link #response()}
 * @param <T> the type
 */
public abstract class ResponseSingleObserver<T> extends ComposableSingleObserver<Response<T>> {

    private Response<T> response;

    /**
     * The response was a success. Fetch the raw response via {@link #response()}
     * @param t the Retrofit response body, which can be null depending on your API
     */
    public abstract void responseSuccess(@Nullable T t);

    @CallSuper
    @Override
    public void success(@NonNull Response<T> response) {
        this.response = response;
        if (!response.isSuccessful()) {
            error(new HttpException(response));
        } else {
            responseSuccess(response.body());
        }
    }

    /**
     * The raw retrofit response. Can be null within {@link #onError(Throwable)} so you will want
     * to check for an {@link HttpException} or check for not {@code null} within {@link #onError(Throwable)}
     * @return the raw response from Retrofit.
     */
    public Response<T> response() {
        return response;
    }
}
