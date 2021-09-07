package com.example.forecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecast.data.db.entity.CurrentWeather
import com.example.forecast.data.db.entity.FutureWeatherEntry
import com.example.forecast.data.db.entity.WeatherLocation

@Database(
    entities = [CurrentWeather::class, FutureWeatherEntry::class,WeatherLocation::class],
    version=1
)

@TypeConverters(LocalDateConverter::class)

abstract class ForecastDatabase: RoomDatabase() {

    abstract fun currentWeatherDao():CurrentWeatherDao
    abstract fun futureWeatherDao():FutureWeatherDao
    abstract fun weatherLocationDao():WeatherLocationDao

    companion object{
        @Volatile private var instance:ForecastDatabase?=null

        //we need a lock so that no more than 1 instance can access it at one time
        private val LOCK=Any()

        operator fun invoke(context: Context)=instance?: synchronized(LOCK){
            instance?: buildDatabase(context).also{ instance=it}
        }

        private fun buildDatabase(context: Context)=
            Room.databaseBuilder(context.applicationContext,
            ForecastDatabase::class.java,"Forecast.db")
                .build()
    }

}