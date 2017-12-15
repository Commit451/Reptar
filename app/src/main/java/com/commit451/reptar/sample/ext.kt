package com.commit451.reptar.sample

import com.commit451.reptar.kotlin.fromIoToMainThread
import com.uber.autodispose.CompletableSubscribeProxy
import com.uber.autodispose.SingleSubscribeProxy
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.Completable
import io.reactivex.Single

fun <T> Single<T>.with(baseActivity: BaseActivity): SingleSubscribeProxy<T> {
    return this.fromIoToMainThread().autoDisposable(baseActivity.scopeProvider)
}

fun Completable.with(baseActivity: BaseActivity): CompletableSubscribeProxy {
    return this.fromIoToMainThread().autoDisposable(baseActivity.scopeProvider)
}