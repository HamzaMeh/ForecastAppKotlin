package com.example.forecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecast.data.network.response.CurrentWeatherResponse
import com.example.forecast.data.network.response.FutureWeatherResponse
import com.example.forecast.internal.NoConnectivityException

const val FORECAST_DAYS_COUNT=7

class WeatherNetworkDataSourceImpl(private val weatherApiService: WeatherApiService) : WeatherNetworkDataSource {
    private val _downloadedCurrentData=MutableLiveData<CurrentWeatherResponse>()
    private val _downloadedFutureData=MutableLiveData<FutureWeatherResponse>()


    override val downloadCurrentData: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentData
    override val downloadFutureData: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureData

    override suspend fun fetchCurrentWeather(location: String) {
        try{
            val fetchedCurrentWeather=weatherApiService
                .getCurrentWeather(location)
                .await()
            _downloadedCurrentData.postValue(fetchedCurrentWeather)
        }catch(e:NoConnectivityException)
        {
            Log.e("Connectivity","No Internet Connection",e)

        }
    }

    override suspend fun fetchFutureWeather(location: String, days: Int) {
        try{
            val fetchedFutureWeather=weatherApiService
                .getFututerWeather(location, days)
                .await()
            _downloadedFutureData.postValue(fetchedFutureWeather)
        }catch(e:NoConnectivityException)
        {
            Log.e("Connectivity","No Internet Connection",e)

        }
    }
}