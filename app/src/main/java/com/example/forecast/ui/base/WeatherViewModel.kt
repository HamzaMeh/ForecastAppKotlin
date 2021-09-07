package com.example.forecast.ui.base

import androidx.lifecycle.ViewModel
import com.example.forecast.data.provider.UNIT_SYSTEM
import com.example.forecast.data.provider.UnitSystemProvider
import com.example.forecast.data.repository.ForecastRepository
import com.example.forecast.internal.Units
import com.example.forecast.internal.lazyDeferred

abstract class WeatherViewModel (
    private val forecastRepository:ForecastRepository,
    unitSystem:UnitSystemProvider
):ViewModel()
{
    private val units=unitSystem.getUnitSystem()

    val isMetricUnit:Boolean
    get() = units== Units.METRIC

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }

}