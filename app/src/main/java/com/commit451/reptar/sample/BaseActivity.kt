package com.commit451.reptar.sample

import android.support.v7.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

abstract class BaseActivity: AppCompatActivity() {

    val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
}
