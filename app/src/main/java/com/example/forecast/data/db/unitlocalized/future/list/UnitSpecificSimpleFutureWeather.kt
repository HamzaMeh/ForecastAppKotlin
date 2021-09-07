package com.example.forecast.data.db.unitlocalized.future.list

import org.threeten.bp.LocalDate


interface UnitSpecificSimpleFutureWeather {
    val date: LocalDate
    val avgTemperature:Double
    val conditionText:String
    val conditionIconUrl:String
}