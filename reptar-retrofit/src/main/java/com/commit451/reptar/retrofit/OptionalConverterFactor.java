package com.commit451.reptar.retrofit;

import com.commit451.reptar.Optional;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * A {@linkplain Converter.Factory converter} for {@code Optional<T>} which delegates to another
 * converter to deserialize {@code T} and then wraps it into {@link Optional}.
 */
public final class OptionalConverterFactor extends Converter.Factory {
    public static OptionalConverterFactor create() {
        return new OptionalConverterFactor();
    }

    private OptionalConverterFactor() {
    }

    @Nullable @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (getRawType(type) != Optional.class) {
            return null;
        }

        Type innerType = getParameterUpperBound(0, (ParameterizedType) type);
        Converter<ResponseBody, Object> delegate =
                retrofit.nextResponseBodyConverter(this, innerType, annotations);
        return new OptionalConverter<>(delegate);
    }
}
