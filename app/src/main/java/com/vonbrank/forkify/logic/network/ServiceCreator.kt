package com.vonbrank.forkify.logic.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vonbrank.forkify.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://forkify-api.herokuapp.com/"

    private lateinit var retrofit: Retrofit

    init {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
        if (BuildConfig.DEBUG)
            builder.client(
                OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build()
            )
        retrofit = builder.build()
    }

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}