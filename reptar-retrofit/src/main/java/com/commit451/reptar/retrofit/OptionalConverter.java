package com.commit451.reptar.retrofit;

import com.commit451.reptar.Optional;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class OptionalConverter<T> implements Converter<ResponseBody, Optional<T>> {
    private final Converter<ResponseBody, T> delegate;

    OptionalConverter(Converter<ResponseBody, T> delegate) {
        this.delegate = delegate;
    }

    @Override public Optional<T> convert(ResponseBody value) throws IOException {
        return new Optional<>(delegate.convert(value));
    }
}