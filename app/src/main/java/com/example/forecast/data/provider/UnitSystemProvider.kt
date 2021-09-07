package com.example.forecast.data.provider

import com.example.forecast.internal.Units

interface UnitSystemProvider {
    fun getUnitSystem():Units
}