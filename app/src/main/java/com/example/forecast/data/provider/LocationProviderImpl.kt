package com.example.forecast.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.forecast.data.db.entity.WeatherLocation
import com.example.forecast.internal.NoLocationPermissionException
import com.example.forecast.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred

const val USE_DEVICE_LOCATION="USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION="CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context) :
    PreferenceProvider(context), LocationProvider {

    private val appContext=context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        val deviceLocationChanged=try {
            hasDeviceLocationChanged(lastWeatherLocation)

        }catch (e:NoLocationPermissionException)
        {
            false
        }
        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    override suspend fun getPreferredLocationString(): String {
        if(isUsingDeviceLocation())
        {
            try {
                val deviceLocation=getDeviceLocation().await()
                    ?:return "${getCustomLocation()}"
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            }catch (e:NoLocationPermissionException)
            {
                return "${getCustomLocation()}"
            }
        }
        else
        {
            return "${getCustomLocation()}"
        }
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation):Boolean
    {
        if(!isUsingDeviceLocation())
            return false
        val deviceLocation=getDeviceLocation().await()
            ?:return false

        val comparisonThreshold=0.03
        return Math.abs(deviceLocation.latitude-lastWeatherLocation.lat)>comparisonThreshold &&
                Math.abs(deviceLocation.longitude-lastWeatherLocation.lon)>comparisonThreshold
    }

    private fun isUsingDeviceLocation():Boolean
    {
        return preferences.getBoolean(USE_DEVICE_LOCATION,true)

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(): Deferred<Location?>
    {
        return if(hasLocationPermission()) fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw NoLocationPermissionException()
    }
    private fun hasLocationPermission():Boolean
    {
        return ContextCompat.checkSelfPermission(appContext,
        Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation):Boolean
    {
        if(!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocation()
            return customLocationName != lastWeatherLocation.name
        }
        return false
    }
    private fun getCustomLocation():String?{
        return preferences.getString(CUSTOM_LOCATION,null)
    }
}