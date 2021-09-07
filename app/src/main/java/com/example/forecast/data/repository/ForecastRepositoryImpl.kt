package com.example.forecast.data.repository

import androidx.lifecycle.LiveData
import com.example.forecast.data.db.CurrentWeatherDao
import com.example.forecast.data.db.FutureWeatherDao
import com.example.forecast.data.db.WeatherLocationDao
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.data.db.unitlocalized.current.UnitSpecificCurrentWeatherEntity
import com.example.forecast.data.db.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.example.forecast.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeather
import com.example.forecast.data.network.FORECAST_DAYS_COUNT
import com.example.forecast.data.network.WeatherNetworkDataSource
import com.example.forecast.data.network.response.CurrentWeatherResponse
import com.example.forecast.data.network.response.FutureWeatherResponse
import com.example.forecast.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init{
        weatherNetworkDataSource.apply {
            downloadCurrentData.observeForever() { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadFutureData.observeForever(){ newFutureWeather->
                persistFetchedFutureWeather(newFutureWeather)
        }
        }
    }
    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntity> {
        return withContext(Dispatchers.IO)
        {
            initWeather()
            return@withContext if(metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeather>> {
        return withContext(Dispatchers.IO) {
            initWeather()
            return@withContext if (metric) futureWeatherDao.getSimpleWeatherForecastMetric(startDate)
            else futureWeatherDao.getSimpleWeatherForecastImperial(startDate)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO)
        {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): LiveData<out UnitSpecificDetailFutureWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeather()
            return@withContext if (metric) futureWeatherDao.getDetailedWeatherByDateMetric(date)
            else futureWeatherDao.getDetailedWeatherByDateImperial(date)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather:CurrentWeatherResponse)
    {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeather)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather:FutureWeatherResponse)
    {
        fun deleteOldForecastData()
        {
            val today=LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }


        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList=fetchedWeather.futureWeatherEntries.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeather()
    {
        val lastWeatherLocation=weatherLocationDao.getLocationNonLive()
        if(lastWeatherLocation==null
            || locationProvider.hasLocationChanged((lastWeatherLocation)))
        {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }
        if(isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if(isFetchFutureNeeded())
            fetchFutureWeather()
    }
    private suspend fun fetchCurrentWeather()
    {
        weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationString())
    }

    private suspend fun fetchFutureWeather()
    {
        weatherNetworkDataSource.fetchFutureWeather(locationProvider.getPreferredLocationString(),
            FORECAST_DAYS_COUNT)
    }

    private fun isFetchCurrentNeeded(lastFetchedTime:ZonedDateTime):Boolean{
        val thirtyMinutesAgo=ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchFutureNeeded():Boolean{
        val today=LocalDate.now()
        val futureWeatherCount=futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount< FORECAST_DAYS_COUNT
    }
}