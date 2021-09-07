package com.example.forecast

import android.app.Application
import android.content.Context
import com.example.forecast.data.db.ForecastDatabase
import com.example.forecast.data.network.*
import com.example.forecast.data.provider.LocationProvider
import com.example.forecast.data.provider.LocationProviderImpl
import com.example.forecast.data.provider.UnitSystemProvider
import com.example.forecast.data.provider.UnitSystemProviderImpl
import com.example.forecast.data.repository.ForecastRepository
import com.example.forecast.data.repository.ForecastRepositoryImpl
import com.example.forecast.ui.weather.current.CurrentWeatherViewModelFactory
import com.example.forecast.ui.weather.future.detail.FutureDetailWeatherViewModelFactory
import com.example.forecast.ui.weather.future.list.FutureWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class ForecastApplication:Application(),KodeinAware {
    override val kodein= Kodein.lazy {
        import(androidModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>())}
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(),instance(),instance(),instance(),instance()) }
        bind<UnitSystemProvider>() with singleton { UnitSystemProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(),instance()) }
        bind() from provider { FutureWeatherViewModelFactory(instance(),instance()) }
        bind() from factory{ detailDate:LocalDate ->FutureDetailWeatherViewModelFactory(detailDate,instance(),instance())}
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }

}