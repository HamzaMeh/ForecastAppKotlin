package com.example.forecast.ui.weather.future.list

import androidx.lifecycle.ViewModel
import com.example.forecast.data.provider.UnitSystemProvider
import com.example.forecast.data.repository.ForecastRepository
import com.example.forecast.internal.lazyDeferred
import com.example.forecast.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitSystem:UnitSystemProvider
) : WeatherViewModel(forecastRepository,unitSystem){

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(),super.isMetricUnit)
    }


}