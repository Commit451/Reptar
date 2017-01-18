package com.commit451.reptar.sample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.ViewGroup
import android.widget.Toast
import com.commit451.reptar.kotlin.fromIoToMainThread
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainKotlinActivity : RxAppCompatActivity() {

    lateinit var root: ViewGroup
    lateinit var gitHub: GitHub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kotlin)

        root = findViewById(R.id.root) as ViewGroup

        val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(GitHub.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        gitHub = retrofit.create(GitHub::class.java)

        gitHub.contributors("jetbrains", "kotlin")
                .compose(bindToLifecycle())
                .fromIoToMainThread()
                .subscribe(object : CustomSingleObserver<List<Contributor>>() {
                    override fun success(t: List<Contributor>) {
                        Snackbar.make(root, "It worked!", Snackbar.LENGTH_SHORT)
                                .show()
                    }

                    override fun error(t: Throwable) {
                        onHandleError(t)
                    }
                })

    }

    private fun onHandleError(e: Throwable) {
        e.printStackTrace()
        Toast.makeText(this@MainKotlinActivity, "Error!!!!", Toast.LENGTH_SHORT).show()
    }
}
