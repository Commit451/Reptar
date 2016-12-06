package com.commit451.reptar.sample;

import android.os.Bundle;
import android.widget.Toast;

import com.commit451.reptar.AdaptableObserver;
import com.commit451.reptar.AdaptableSingleObserver;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends RxAppCompatActivity {

    public static final String API_URL = "https://api.github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        GitHub gitHub = retrofit.create(GitHub.class);

        gitHub.contributors("square", "retrofit")
                .compose(this.<List<Contributor>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AdaptableSingleObserver<List<Contributor>>() {

                    @Override
                    public void onSuccess(List<Contributor> value) {
                        Toast.makeText(MainActivity.this, "There are " + value.size() + " contributors to Retrofit!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error!!!!", Toast.LENGTH_SHORT).show();
                    }
                });

        gitHub.contributorsObservable("square", "okhttp")
                .compose(this.<List<Contributor>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AdaptableObserver<List<Contributor>>() {
                    @Override
                    public void onNext(List<Contributor> value) {
                        super.onNext(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }
}
