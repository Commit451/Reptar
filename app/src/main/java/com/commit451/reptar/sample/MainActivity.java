package com.commit451.reptar.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.commit451.reptar.AdaptableSingleObserver;
import com.commit451.reptar.retrofit.ResponseSingleObserver;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends RxAppCompatActivity {

    public static final String API_URL = "https://api.github.com";

    GitHub gitHub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        gitHub = retrofit.create(GitHub.class);

        findViewById(R.id.button_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gitHub.contributors("square", "retrofit")
                        .compose(MainActivity.this.<List<Contributor>>bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new AdaptableSingleObserver<List<Contributor>>() {

                            @Override
                            public void onSuccess(List<Contributor> value) {
                                Toast.makeText(MainActivity.this, "There are " + value.size() + " contributors to Retrofit!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
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
                            protected void onResponseSuccess(List<Contributor> contributors) {
                                Toast.makeText(MainActivity.this, "Response code:" + response().code(), Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                onHandleError(e);
                            }
                        });
            }
        });
    }

    private void onHandleError(Throwable e) {
        e.printStackTrace();
        Toast.makeText(MainActivity.this, "Error!!!!", Toast.LENGTH_SHORT).show();
    }
}
