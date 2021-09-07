package com.example.forecast.ui.weather.current

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.forecast.R
import com.example.forecast.internal.glide.GlideApp
import com.example.forecast.ui.base.ScopeFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeather : ScopeFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory :CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI()=launch{
        val currentWeather=viewModel.weather.await()
        val currentWeatherLocation=viewModel.weatherLocation.await()


        currentWeatherLocation.observe(viewLifecycleOwner, Observer { location->
            if(location ==null) return@Observer
            updateLocation(location.name)
        })



        currentWeather.observe(viewLifecycleOwner, Observer {
          if(it==null) return@Observer
                group_loading.visibility=View.GONE
            updateLocation("Lahore")
            updateDateToday()
            updateTemperature(it.temperature, it.feelsLikeTemperature)
            updateCondition(it.conditionText)
            updatePrecipitation(it.precipitationVolume)
            updateWind(it.windDirection, it.windSpeed)
            updateVisibility(it.visibilityDistance)
            GlideApp.with(this@CurrentWeather)
                .load("https:${it.conditionIconUrl}")
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalisedUnitAbbreviation(metric:String, imperial:String):String
    {
        return if(viewModel.isMetricUnit) metric else imperial
    }

    private fun updateLocation(location:String)
    {
        (activity as? AppCompatActivity)?.supportActionBar?.title=location
    }
    private fun updateDateToday()
    {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle="Today"
    }

    private fun updateTemperature(temperature:Double, feelsLike:Double)
    {
        val unitAbbreviation=chooseLocalisedUnitAbbreviation("C","F")
        textView_temperature.text="$temperature $unitAbbreviation"
        textView_feels_like_temperature.text="Feels like $feelsLike $unitAbbreviation"
    }

    private fun updateCondition(condition:String)
    {
        textView_condition.text=condition
    }
    private fun updatePrecipitation(precipitationVolume:Double)
    {
        val unitAbbreviation=chooseLocalisedUnitAbbreviation("mm","in")
        textView_precipitation.text=" Precipitation $precipitationVolume $unitAbbreviation"
    }
    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalisedUnitAbbreviation("kph", "mph")
        textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalisedUnitAbbreviation("km", "mi")
        textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
    }
}