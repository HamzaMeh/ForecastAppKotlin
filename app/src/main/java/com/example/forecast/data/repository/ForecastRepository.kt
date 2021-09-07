package com.example.forecast.data.repository

import androidx.lifecycle.LiveData
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntity
import com.example.forecast.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.example.forecast.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeather
import org.threeten.bp.LocalDate

interface ForecastRepository {
    suspend fun getCurrentWeather(metric:Boolean): LiveData<out UnitSpecificCurrentWeatherEntity>

    suspend fun getFutureWeatherList(startDate:LocalDate,metric: Boolean):LiveData<out List<UnitSpecificSimpleFutureWeather>>

    suspend fun getFutureWeatherByDate(date: LocalDate,metric: Boolean):LiveData<out  UnitSpecificDetailFutureWeatherEntry>

    suspend fun getWeatherLocation():LiveData<WeatherLocation>
}