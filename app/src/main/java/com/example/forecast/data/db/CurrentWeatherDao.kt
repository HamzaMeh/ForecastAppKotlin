package com.example.forecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.forecast.data.db.entity.CURRENT_WEATHER_ID
import com.example.forecast.data.db.entity.CurrentWeather
import com.example.forecast.data.db.unitlocalized.current.ImperialCurrentWeatherEntity
import com.example.forecast.data.db.unitlocalized.current.MetricCurrentWeatherEntity

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = REPLACE)
    fun upsert(weatherEntity: CurrentWeather)

    @Query("select * from current_weather where id=$CURRENT_WEATHER_ID")
    fun getWeatherMetric():LiveData<MetricCurrentWeatherEntity>

    @Query("select * from current_weather where id=$CURRENT_WEATHER_ID")
    fun getWeatherImperial():LiveData<ImperialCurrentWeatherEntity>
}