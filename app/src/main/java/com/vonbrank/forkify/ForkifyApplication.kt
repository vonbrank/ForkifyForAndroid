package com.vonbrank.forkify

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho

class ForkifyApplication : Application() {
    companion object {
        const val TOKEN = "8d488d17-fae0-474f-a48f-b7eab7d8c578"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }
}