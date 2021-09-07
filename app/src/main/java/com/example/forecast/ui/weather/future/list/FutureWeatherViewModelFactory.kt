package com.example.forecast.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forecast.data.provider.UnitSystemProvider
import com.example.forecast.data.repository.ForecastRepository

class FutureWeatherViewModelFactory (
    private val forecastRepository:ForecastRepository,
    private val unitSystem:UnitSystemProvider
):ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureWeatherViewModel(forecastRepository,
        unitSystem) as T
    }

}