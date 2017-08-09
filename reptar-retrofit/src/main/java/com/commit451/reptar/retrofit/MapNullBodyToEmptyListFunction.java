package com.commit451.reptar.retrofit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Maps a null body from retrofit which is expected to be a list to an empty list
 * @param <T> type
 */
public class MapNullBodyToEmptyListFunction<T> implements Function<Response<List<T>>, SingleSource<List<T>>> {

    @Override
    public SingleSource<List<T>> apply(@NonNull Response<List<T>> response) throws Exception {
        if (!response.isSuccessful()) {
            throw new HttpException(response);
        } else {
            List<T> body = response.body();
            if (body == null) {
                List<T> items = new ArrayList<>();
                return Single.just(items);
            } else {
                return Single.just(body);
            }
        }
    }
}
