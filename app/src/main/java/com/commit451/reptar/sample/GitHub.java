package com.commit451.reptar.sample;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GitHub {

    @GET("/repos/{owner}/{repo}/contributors")
    Single<List<Contributor>> contributors(@Path("owner") String owner,
                                           @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributorsObservable(@Path("owner") String owner,
                                                         @Path("repo") String repo);
}
