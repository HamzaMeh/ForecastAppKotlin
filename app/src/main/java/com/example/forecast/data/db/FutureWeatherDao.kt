package com.example.forecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecast.data.db.entity.FutureWeatherEntry
import com.example.forecast.data.db.unitlocalized.future.detail.ImperialDetailFutureWeatherEntry
import com.example.forecast.data.db.unitlocalized.future.detail.MetricDetailFutureWeatherEntry
import com.example.forecast.data.db.unitlocalized.future.list.ImperialSimpleFutureWeather
import com.example.forecast.data.db.unitlocalized.future.list.MetricSimpleFutureWeatherClass
import org.threeten.bp.LocalDate
@Dao
interface FutureWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("Select * from future_Weather where date(date)>=date(:startDate)")
    fun getSimpleWeatherForecastImperial(startDate:LocalDate):LiveData<List<ImperialSimpleFutureWeather>>


    @Query("Select * from future_Weather where date(date)>=date(:startDate)")
    fun getSimpleWeatherForecastMetric(startDate: LocalDate):LiveData<List<MetricSimpleFutureWeatherClass>>

    @Query("Select count(id) from future_Weather where date(date)>=date(:startDate)")
    fun countFutureWeather(startDate:LocalDate):Int

    @Query("delete from future_Weather where date(date)<date(:firstdatetokeep)")
    fun deleteOldEntries(firstdatetokeep:LocalDate)

    @Query("select * from future_weather where date(date) = date(:date)")
    fun getDetailedWeatherByDateMetric(date: LocalDate): LiveData<MetricDetailFutureWeatherEntry>

    @Query("select * from future_weather where date(date) = date(:date)")
    fun getDetailedWeatherByDateImperial(date: LocalDate): LiveData<ImperialDetailFutureWeatherEntry>

}