package com.hao.hweather2.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.repository.WeatherRepository
import com.hao.hweather2.utils.MySharedPreferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    val dataWeatherCity : LiveData<DataWeatherCity>
    get() = weatherRepository.weather

    val dataWeatherCity_Location : LiveData<DataWeatherCity>
        get() = weatherRepository.weatherLocation

    fun getOneWeather(nameCity:String, lang:String) = GlobalScope.launch {
        weatherRepository.getOneWeather(nameCity,lang)
    }

    fun getWeatherLocation(lat : String,lon : String,lang : String) = GlobalScope.launch {
        weatherRepository.getWeatherByLocation(lat,lon,lang)
    }

    fun insertWeather(nameCity:String, lang:String) = GlobalScope.launch {
        weatherRepository.getWeather(nameCity,lang)
    }

    fun getCountRecord() : Int = weatherRepository.getCountRecord()

    fun getTopRecord() : DataWeatherCity = weatherRepository.getTopRecord()

    fun getRecord(id : Int) : DataWeatherCity = weatherRepository.getRecord(id)

    fun getAllWeather() : LiveData<List<DataWeatherCity>> = weatherRepository.getAllWeather()

    fun getAlRecord() : List<DataWeatherCity> = weatherRepository.getAllRecord()

    fun updateWeather(dataWeatherCity: DataWeatherCity) = GlobalScope.launch {
        weatherRepository.updateDataWeather(dataWeatherCity)
    }

    fun deleteWeather(dataWeatherCity: DataWeatherCity) = GlobalScope.launch {
        weatherRepository.deleteDataWeather(dataWeatherCity)
    }
}