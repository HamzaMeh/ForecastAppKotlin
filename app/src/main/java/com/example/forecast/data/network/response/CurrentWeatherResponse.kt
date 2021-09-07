package com.example.forecast.data.network.response

import com.example.forecast.data.db.entity.CurrentWeather
import com.example.forecast.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeather: CurrentWeather,
    val location: WeatherLocation
)