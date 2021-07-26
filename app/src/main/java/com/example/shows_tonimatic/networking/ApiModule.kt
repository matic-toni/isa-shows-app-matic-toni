package com.example.shows_tonimatic.networking

import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ApiModule {
    private const val BASE_URL = "https://tv-shows.infinum.academy"

    lateinit var retrofit: ShowsApiService
    lateinit var prefs: SharedPreferences

    fun initRetrofit(preferences: SharedPreferences) {
        prefs = preferences
        lateinit var okhttp: OkHttpClient

        if (prefs.getString("access-token", "") != "") {
           okhttp = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(
                    Interceptor {
                            chain ->
                        val builder = chain.request().newBuilder()
                        builder.header("Accept", "application/json")
                        builder.header("access-token", prefs.getString("access-token", "")!!)
                        builder.header("client", prefs.getString("client", "")!!)
                        builder.header("token-type", prefs.getString("token-type", "")!!)
                        builder.header("expiry", prefs.getString("expiry", "")!!)
                        builder.header("uid", prefs.getString("uid", "")!!)
                        builder.header("Content-Type", prefs.getString("content-type", "")!!)
                        return@Interceptor chain.proceed(builder.build())
                    }
                )
                .build()
        } else {
            okhttp = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okhttp)
            .build()
            .create(ShowsApiService::class.java)
    }
}
