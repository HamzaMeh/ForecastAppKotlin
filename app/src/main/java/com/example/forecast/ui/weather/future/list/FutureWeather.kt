package com.example.forecast.ui.weather.future.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecast.R
import com.example.forecast.data.db.LocalDateConverter
import com.example.forecast.data.db.unitlocalized.future.list.UnitSpecificSimpleFutureWeather
import com.example.forecast.ui.base.ScopeFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.current_weather_fragment.group_loading
import kotlinx.android.synthetic.main.future_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.threeten.bp.LocalDate

class FutureWeather : ScopeFragment(),KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory:FutureWeatherViewModelFactory by instance()

    private lateinit var viewModel: FutureWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory
        ).get(FutureWeatherViewModel::class.java)
       bindUI()
    }

    private fun bindUI()=launch(Dispatchers.Main) {
        val futureWeatherEntries=viewModel.weatherEntries.await()
        val weatherLocation=viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location->
            if(location==null) return@Observer
            updateLocation(location.name)
        })

        futureWeatherEntries.observe(viewLifecycleOwner, Observer { weatherEntries->
            if(weatherEntries==null) return@Observer

            group_loading.visibility=View.GONE
            updateDateToNextWeek()
            initRecyclerView(weatherEntries.toFutureWeatherItem())
        })
    }

    private fun updateLocation(location:String){
        (activity as? AppCompatActivity)?.supportActionBar?.title=location
    }

    private fun updateDateToNextWeek(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle="Next Week"
    }

    private fun List<UnitSpecificSimpleFutureWeather>.toFutureWeatherItem():List<FutureItem>{
        return this.map {
            FutureItem(it)
        }
    }

    private fun initRecyclerView(items:List<FutureItem>){
        val groupAdapter=GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FutureWeather.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? FutureItem)?.let {
                showWeatherDetail(it.weatherEntry.date, view)
            }
        }
    }

    private fun showWeatherDetail(date: LocalDate,view:View)
    {
        val dateString=LocalDateConverter.dateToString(date)!!
        val actionDetail=FutureWeatherDirections.actionDetail(dateString)
        Navigation.findNavController(view).navigate(actionDetail)

    }


}