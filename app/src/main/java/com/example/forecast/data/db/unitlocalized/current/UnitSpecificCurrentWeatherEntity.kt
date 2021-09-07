package com.example.forecast.data.db.unitlocalized.current

interface UnitSpecificCurrentWeatherEntity {
    val temperature:Double
    val conditionText:String
    val conditionIconUrl:String
    val windSpeed:Double
    val windDirection:String
    val precipitationVolume:Double
    val feelsLikeTemperature:Double
    val visibilityDistance:Double
}