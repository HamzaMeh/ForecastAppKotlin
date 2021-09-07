package com.example.forecast.data.db.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "future_Weather",indices = [Index("date",unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val date: String,
    @Embedded
    val day: Day
)