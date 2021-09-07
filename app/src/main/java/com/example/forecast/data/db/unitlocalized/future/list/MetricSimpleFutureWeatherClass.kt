package com.example.forecast.data.db.unitlocalized.future.list

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate


data class MetricSimpleFutureWeatherClass(
    @ColumnInfo(name="date")
    override val date: LocalDate,
    @ColumnInfo(name="avgTempC")
    override val avgTemperature: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String
): UnitSpecificSimpleFutureWeather
