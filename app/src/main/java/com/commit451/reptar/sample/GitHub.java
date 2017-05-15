package com.commit451.reptar.sample;

import com.commit451.reptar.Optional;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GitHub {

    String API_URL = "https://api.github.com";

    @GET("/repos/{owner}/{repo}/contributors")
    Single<List<Contributor>> contributors(@Path("owner") String owner,
                                           @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    Single<Response<List<Contributor>>> contributorsResponse(@Path("owner") String owner,
                                                             @Path("repo") String repo);

    @GET("/emojis")
    Completable emojis();

    @GET("/emojis")
    Single<Optional<List<Contributor>>> contributorsOrNull();

    /**
     * Requires auth. We use this to test failure
     * @return a failed response
     */
    @GET("/users/orgs")
    Single<Response<ResponseBody>> orgs();
}
