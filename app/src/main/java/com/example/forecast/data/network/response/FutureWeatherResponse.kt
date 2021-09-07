package com.example.forecast.data.network.response


import com.example.forecast.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    @SerializedName("forecast")
    val futureWeatherEntries: ForecastDayContainer,
    val location: WeatherLocation
)