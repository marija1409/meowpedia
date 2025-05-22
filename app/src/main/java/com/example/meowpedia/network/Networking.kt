package com.example.meowpedia.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private val appJson = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor {
        val updatedRequest = it.request().newBuilder()
            .addHeader("CustomHeader", "CustomValue")
            .addHeader("x-api-key", "live_Ouzr56q1jEfpak0yBCxLNfOWKkj2QsIauzeYpasboh04Nb96LQIbGsgIVHuX5Me8")
            .build()
        it.proceed(updatedRequest)
    }
    .addInterceptor(
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    )
    .build()


val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.thecatapi.com/v1/")
    .client(okHttpClient)
    .addConverterFactory(appJson.asConverterFactory("application/json".toMediaType()))
    .build()
