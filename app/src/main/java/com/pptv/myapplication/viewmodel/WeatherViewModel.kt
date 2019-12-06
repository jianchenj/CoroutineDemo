package com.pptv.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.pptv.myapplication.model.Weather
import com.pptv.myapplication.repository.HttpRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async

class WeatherViewModel : BaseViewModel() {
    val mWeather: MutableLiveData<Weather> = MutableLiveData()

    fun getWeather(start: () -> Unit, finally: () -> Unit) {
        launchOnUITryCatch(
            {
                start()
                val weather = async(IO) { HttpRepository.getWeather() }.await()
                mWeather.value = weather.await()
            },
            {
                Log.i("test", "error! ${it.message}")
            }, {
                finally()
            }, true)
    }
}