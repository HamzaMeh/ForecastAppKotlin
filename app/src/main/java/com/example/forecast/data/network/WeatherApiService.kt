package com.example.forecast.data.network

import com.example.forecast.data.network.ConnectivityInterceptor
import com.example.forecast.data.network.ConnectivityInterceptorImpl
import com.example.forecast.data.network.response.CurrentWeatherResponse
import com.example.forecast.data.network.response.FutureWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val api_key="527c63956baa40678e0111245210109"



interface WeatherApiService {

    //https://api.weatherapi.com/v1/current.json?key=527c63956baa40678e0111245210109&q=London&aqi=no
    @GET("current.json")
    fun getCurrentWeather(
        @Query("q")location:String
    ): Deferred<CurrentWeatherResponse> //this is return type, which is API response.. since , result takes time, we will handle this in deferred coroutine


    //https://api.weatherapi.com/v1/forecast.json?key=527c63956baa40678e0111245210109&q=Lahore&days=1&aqi=no&alerts=no
    @GET("forecast.json")
    fun getFututerWeather(
        @Query("q")location: String,
        @Query("days")days:Int,
    ):Deferred<FutureWeatherResponse>

    companion object{
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ):WeatherApiService{
            val requestInterceptor=Interceptor { chain ->
                val url=chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", api_key)
                    .build()
                val request=chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)

            }

            val okHttpClient=OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.weatherapi.com/v1/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}