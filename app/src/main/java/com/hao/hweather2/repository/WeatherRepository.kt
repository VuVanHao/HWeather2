package com.hao.hweather2.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hao.hweather2.database.FakerDatabase
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.retrofit.IWeatherServices
import com.hao.hweather2.utils.Constants
import okhttp3.Response
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class WeatherRepository @Inject constructor(
    private val iWeatherServices: IWeatherServices,
    private val fakerDatabase: FakerDatabase) {

    private val _weather = MutableLiveData<DataWeatherCity>()
    val weather : LiveData<DataWeatherCity> get() = _weather

    private val _weatherLocation = MutableLiveData<DataWeatherCity>()
    val weatherLocation : LiveData<DataWeatherCity> get() = _weatherLocation

    suspend fun getWeather( nameCity:String, lang:String)
    {
        val result = iWeatherServices.getWeatherByNameCity(nameCity,lang,Constants.apiKey)
        if (result.isSuccessful && result.body() != null)
        {
            fakerDatabase.getWeatherDAO().addWeather(result.body()!!)
        }
    }

    suspend fun getOneWeather( nameCity:String, lang:String)
    {
        val result = iWeatherServices.getWeatherByNameCity(nameCity,lang,Constants.apiKey)
        if (result.isSuccessful && result.body() != null)
        {
            _weather.postValue(result.body())
        }
    }

    suspend fun getWeatherByLocation( lat:String, lon:String, lang : String)
    {
        val result = iWeatherServices.getWeatherByLocation(lat,lon,lang,Constants.apiKey)
        if (result.isSuccessful && result.body() != null)
        {
            _weatherLocation.postValue(result.body())
        }
    }



    fun getAllWeather(): LiveData<List<DataWeatherCity>> = fakerDatabase.getWeatherDAO().getAllWeather()

    fun getAllRecord(): List<DataWeatherCity> = fakerDatabase.getWeatherDAO().getAllRecord()

    fun getCountRecord() : Int = fakerDatabase.getWeatherDAO().getCountRecord()

    fun getTopRecord() : DataWeatherCity = fakerDatabase.getWeatherDAO().getTopRecord()

    fun getRecord(id : Int) : DataWeatherCity = fakerDatabase.getWeatherDAO().getRecord(id)

    suspend fun updateDataWeather(dataWeatherCity: DataWeatherCity) = fakerDatabase.getWeatherDAO().updateDataWeather(dataWeatherCity)

    suspend fun deleteDataWeather(dataWeatherCity: DataWeatherCity) = fakerDatabase.getWeatherDAO().deleteDataWeather(dataWeatherCity)




}