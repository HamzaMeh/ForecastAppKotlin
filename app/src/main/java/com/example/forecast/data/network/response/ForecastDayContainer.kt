package com.example.forecast.data.network.response

import com.example.forecast.data.db.entity.FutureWeatherEntry
import com.google.gson.annotations.SerializedName


data class ForecastDayContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)