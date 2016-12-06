package com.commit451.reptar.retrofit;

import android.support.annotation.CallSuper;

import com.commit451.reptar.SimpleSingleObserver;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import retrofit2.Response;

/**
 * Subscriber that sends HTTP error codes into the {@link #onError(Throwable)}
 * block, but also allows access to the Retrofit response. This is useful
 * for cases where you do not need or want to still check {@link Response#isSuccessful()}
 * but also would like to be able to access the Retrofit response in your success block (via {@link #response()}
 */
public abstract class ResponseSingleObserver<T> extends SimpleSingleObserver<Response<T>> {

    private Response response;

    protected abstract void onResponseSuccess(T t);

    @CallSuper
    @Override
    public void onSuccess(Response<T> response) {
        this.response = response;
        if (!response.isSuccessful()) {
            onError(new HttpException(response));
        } else {
            onResponseSuccess(response.body());
        }
    }

    public Response response() {
        return response;
    }
}
