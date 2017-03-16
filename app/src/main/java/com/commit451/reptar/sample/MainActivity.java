package com.commit451.reptar.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.commit451.reptar.AdaptableSingleObserver;
import com.commit451.reptar.CancellationFailureChecker;
import com.commit451.reptar.ComposableSingleObserver;
import com.commit451.reptar.Result;
import com.commit451.reptar.retrofit.ResponseFunction;
import com.commit451.reptar.retrofit.ResponseSingleObserver;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends RxAppCompatActivity {

    ViewGroup root;

    GitHub gitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GitHub.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        gitHub = retrofit.create(GitHub.class);

        root = (ViewGroup) findViewById(R.id.root);

        findViewById(R.id.button_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gitHub.contributors("square", "retrofit")
                        .doOnSuccess(new Consumer<List<Contributor>>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull List<Contributor> contributors) throws Exception {
                                Log.d("TEST", "doOnSuccess");
                                Toast.makeText(MainActivity.this, "Toasty!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .compose(MainActivity.this.<List<Contributor>>bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new AdaptableSingleObserver<List<Contributor>>() {

                            @Override
                            public void onSuccess(List<Contributor> value) {
                                Log.d("TEST", "onSuccess");
                                Snackbar.make(root, "There are " + value.size() + " contributors to Retrofit!", Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                onHandleError(e);
                            }
                        });
            }
        });

        findViewById(R.id.button_single_focused).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gitHub.contributors("Commit451", "Reptar")
                        .compose(MainActivity.this.<List<Contributor>>bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ComposableSingleObserver<List<Contributor>>() {

                            @Override
                            public void success(@NonNull List<Contributor> contributors) {
                                Snackbar.make(root, "There are " + contributors.size() + " contributors to Reptar!", Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void error(@NonNull Throwable e) {
                                onHandleError(e);
                            }
                        });
            }
        });

        findViewById(R.id.button_response_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gitHub.contributorsResponse("square", "okhttp")
                        .compose(MainActivity.this.<Response<List<Contributor>>>bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResponseSingleObserver<List<Contributor>>() {

                            @Override
                            public void responseSuccess(@Nullable List<Contributor> contributors) {
                                Snackbar.make(root, "Response code:" + response().code(), Snackbar.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void error(@NonNull Throwable t) {
                                onHandleError(t);
                            }
                        });
            }
        });

        Random random = new Random();
        findViewById(R.id.button_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Result<String> result;
                if (random.nextInt() % 2 == 0) {
                    result = new Result<>("hi");
                } else {
                    result = Result.empty();
                }
                Single.just(result)
                        .subscribe(new ComposableSingleObserver<Result<String>>() {

                            @Override
                            public void success(@NonNull Result<String> stringResult) {
                                if (result.isPresent()) {
                                    Snackbar.make(root, "Has a result", Snackbar.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Snackbar.make(root, "No result", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            }

                            @Override
                            public void error(@NonNull Throwable t) {
                                onHandleError(t);
                            }
                        });
            }
        });

        findViewById(R.id.button_cancellation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observables.cancellation()
                        .subscribe(new ComposableSingleObserver<Boolean>() {
                            @Override
                            public void success(@NonNull Boolean aBoolean) {
                                //nope
                            }

                            @Override
                            public void error(@NonNull Throwable t) {
                                //Will never get the error. Swallowed right up
                                onHandleError(t);
                            }
                        }.add(new CancellationFailureChecker()));
            }
        });

        findViewById(R.id.button_flatmap_response).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gitHub.orgs()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new ResponseFunction<ResponseBody, SingleSource<String>>() {
                            @Override
                            protected SingleSource<String> applyResponse(@Nullable ResponseBody responseBody) throws Exception {
                                return Single.just("It will never actually get here");
                            }
                        })
                        .subscribe(new ComposableSingleObserver<String>() {
                            @Override
                            public void success(@NonNull String s) {
                                Snackbar.make(root, "Success?! This is bad", Snackbar.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void error(@NonNull Throwable t) {
                                t.printStackTrace();
                                Snackbar.make(root, "We got a failure from the flat map. Good!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });

        findViewById(R.id.button_kotlin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainKotlinActivity.class));
            }
        });
    }

    private void onHandleError(Throwable e) {
        e.printStackTrace();
        Toast.makeText(MainActivity.this, "Error!!!!", Toast.LENGTH_SHORT).show();
    }
}
