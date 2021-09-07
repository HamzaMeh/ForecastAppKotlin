package com.example.forecast.ui

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LifeCycleBoundManager(lifeCycleOwner:LifecycleOwner,
private val fusedLocationProviderClient: FusedLocationProviderClient,
private val locationCallback: LocationCallback)
    :LifecycleObserver{

        init{
            lifeCycleOwner.lifecycle.addObserver(this)
        }

    private val locationRequests=LocationRequest().apply {
        interval=5000
        fastestInterval=5000
        priority=LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @SuppressLint("MissingPermission")
    fun startLocationUpdates()
    {
        fusedLocationProviderClient.requestLocationUpdates(locationRequests,locationCallback,null)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeLocationUpdates()
    {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}