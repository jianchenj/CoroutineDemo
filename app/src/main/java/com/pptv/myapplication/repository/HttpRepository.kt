package com.pptv.myapplication.repository

import com.google.gson.GsonBuilder
import com.pptv.myapplication.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//单例
object HttpRepository {
    private fun getApiService(): Api {
        return Retrofit.Builder().baseUrl("https://raw.githubusercontent.com/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            //.client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(Api::class.java)
    }

    private fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder().apply { addInterceptor(interceptor) }.build()

    fun getWeather() = getApiService().getWeather()
}