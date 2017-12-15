package com.commit451.reptar.sample

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.commit451.reptar.*
import com.commit451.reptar.Optional
import com.commit451.reptar.retrofit.OptionalConverterFactor
import com.commit451.reptar.retrofit.ResponseFunction
import com.commit451.reptar.retrofit.ResponseSingleObserver
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    lateinit var root: ViewGroup

    lateinit var gitHub: GitHub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(GitHub.API_URL)
                .client(client)
                .addConverterFactory(OptionalConverterFactor.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        gitHub = retrofit.create(GitHub::class.java)

        root = findViewById(R.id.root)

        findViewById<View>(R.id.button_single).setOnClickListener({ _ ->
            gitHub.contributors("square", "retrofit")
                    .doOnSuccess { _ ->
                        Log.d("TEST", "doOnSuccess is in background thread")
                    }
                    .with(this)
                    .subscribe(object : AdaptableSingleObserver<List<Contributor>>() {

                        override fun onSuccess(value: List<Contributor>) {
                            Log.d("TEST", "onSuccess")
                            Snackbar.make(root, "There are " + value.size + " contributors to Retrofit!", Snackbar.LENGTH_SHORT).show()
                        }

                        override fun onError(e: Throwable) {
                            onHandleError(e)
                        }
                    })
        })

        findViewById<View>(R.id.button_single_focused).setOnClickListener({ _ ->
            gitHub.contributors("Commit451", "Reptar")
                    .with(this)
                    .subscribe(object : ComposableSingleObserver<List<Contributor>>() {

                        override fun success(contributors: List<Contributor>) {
                            Snackbar.make(root, "There are " + contributors.size + " contributors to Reptar!", Snackbar.LENGTH_SHORT).show()
                        }

                        override fun error(e: Throwable) {
                            onHandleError(e)
                        }
                    })
        })

        findViewById<View>(R.id.button_response_single).setOnClickListener({ _ ->
            gitHub.contributorsResponse("square", "okhttp")
                    .with(this)
                    .subscribe(object : ResponseSingleObserver<List<Contributor>>() {

                        override fun responseSuccess(contributors: List<Contributor>?) {
                            Snackbar.make(root, "Response code:" + response().code(), Snackbar.LENGTH_SHORT)
                                    .show()
                        }

                        override fun error(t: Throwable) {
                            onHandleError(t)
                        }
                    })
        })

        val random = Random()
        findViewById<View>(R.id.button_result).setOnClickListener({ _ ->
            val optional: Optional<String>
            if (random.nextInt() % 2 == 0) {
                optional = Optional("hi")
            } else {
                optional = Optional.empty()
            }
            Single.just(optional)
                    .with(this)
                    .subscribe(object : ComposableSingleObserver<Optional<String>>() {

                        override fun success(stringResult: Optional<String>) {
                            if (optional.isPresent) {
                                Snackbar.make(root, "Has an optional", Snackbar.LENGTH_SHORT)
                                        .show()
                            } else {
                                Snackbar.make(root, "No optional", Snackbar.LENGTH_SHORT)
                                        .show()
                            }
                        }

                        override fun error(t: Throwable) {
                            onHandleError(t)
                        }
                    })
        })

        findViewById<View>(R.id.button_optional_from_api).setOnClickListener({ _ ->
            gitHub.contributorsOrNull("Commit451", "Reptar")
                    .with(this)
                    .subscribe(object : CustomSingleObserver<Optional<List<Contributor>>>() {
                        override fun success(contributors: Optional<List<Contributor>>) {
                            if (contributors.isPresent) {
                                Snackbar.make(root, "Has an optional", Snackbar.LENGTH_SHORT)
                                        .show()
                            } else {
                                Snackbar.make(root, "No optional", Snackbar.LENGTH_SHORT)
                                        .show()
                            }
                        }

                        override fun error(t: Throwable) {
                            onHandleError(t)
                        }
                    })
        })

        findViewById<View>(R.id.button_cancellation).setOnClickListener({ _ ->
            Observables.cancellation()
                    .with(this)
                    .subscribe(object : ComposableSingleObserver<Boolean>() {
                        override fun success(aBoolean: Boolean) {
                            //nope
                        }

                        override fun error(t: Throwable) {
                            //Will never get the error. Swallowed right up
                            onHandleError(t)
                        }
                    }.add(CancellationFailureChecker()))
        })

        findViewById<View>(R.id.button_flatmap_response).setOnClickListener({ _ ->
            gitHub.orgs()
                    .flatMap(object : ResponseFunction<ResponseBody, SingleSource<String>>() {
                        @Throws(Exception::class)
                        override fun applyResponse(responseBody: ResponseBody?): SingleSource<String> {
                            return Single.just("It will never actually get here")
                        }
                    })
                    .with(this)
                    .subscribe(object : ComposableSingleObserver<String>() {
                        override fun success(s: String) {
                            Snackbar.make(root, "Success?! This is bad", Snackbar.LENGTH_SHORT)
                                    .show()
                        }

                        override fun error(t: Throwable) {
                            t.printStackTrace()
                            Snackbar.make(root, "We got a failure from the flat map. Good!", Snackbar.LENGTH_SHORT)
                                    .show()
                        }
                    })
        })

        findViewById<View>(R.id.button_completion).setOnClickListener({ _ ->
            gitHub.emojis()
                    .with(this)
                    .subscribe(object : ComposableCompletableObserver() {
                        override fun complete() {
                            Snackbar.make(root, "Completable success", Snackbar.LENGTH_SHORT)
                                    .show()
                        }

                        override fun error(t: Throwable) {
                            onHandleError(t)
                        }
                    })
        })

        findViewById<View>(R.id.button_kotlin).setOnClickListener({ _ -> startActivity(Intent(this@MainActivity, MainKotlinActivity::class.java)) })

        Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose { Log.i(TAG, "Disposing subscription from onCreate()") }
                .autoDisposable(scopeProvider)
                .subscribe{ num -> Log.i(TAG, "Started in onCreate(), running until onDestroy(): $num") }
    }

    private fun onHandleError(e: Throwable) {
        e.printStackTrace()
        Toast.makeText(this@MainActivity, "Error!!!!", Toast.LENGTH_SHORT).show()
    }
}
