package com.pptv.myapplication.repository

import com.pptv.myapplication.model.Weather
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface Api {
    @GET("localhost/test/test.json")
    fun getWeather() : Deferred<Weather>
}