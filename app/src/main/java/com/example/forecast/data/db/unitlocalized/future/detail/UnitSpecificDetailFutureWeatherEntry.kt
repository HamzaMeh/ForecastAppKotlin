package com.example.forecast.data.db.unitlocalized.future.detail

import org.threeten.bp.LocalDate


interface UnitSpecificDetailFutureWeatherEntry {
    val date: LocalDate
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val maxWindSpeed: Double
    val totalPrecipitation: Double
    val avgVisibilityDistance: Double
    val uv: Double
}