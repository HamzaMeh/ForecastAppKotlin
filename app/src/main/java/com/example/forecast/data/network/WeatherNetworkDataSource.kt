package com.example.forecast.data.network

import androidx.lifecycle.LiveData
import com.example.forecast.data.network.response.CurrentWeatherResponse
import com.example.forecast.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentData:LiveData<CurrentWeatherResponse>
    val downloadFutureData:LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location:String
        )

    suspend fun fetchFutureWeather(
        location: String,
        days:Int
    )
}