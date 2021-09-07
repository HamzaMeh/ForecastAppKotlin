package com.example.forecast.data.provider

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.forecast.internal.Units

const val UNIT_SYSTEM="UNIT_PREFERENCES"
class UnitSystemProviderImpl(context: Context) : PreferenceProvider(context),UnitSystemProvider {

    override fun getUnitSystem(): Units {
        val selectedName=preferences.getString(UNIT_SYSTEM,Units.METRIC.name)
        return Units.valueOf(selectedName!!)
    }
}