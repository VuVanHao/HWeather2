package com.hao.hweather2.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hao.hweather2.model.DataWeatherCity

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeather(dataWeatherCity: DataWeatherCity)

    @Query("select * from DataWeatherCity")
    fun getAllWeather() : LiveData<List<DataWeatherCity>>

    @Query("select * from DataWeatherCity")
    fun getAllRecord() : List<DataWeatherCity>

    @Query("select COUNT(id_weather) FROM DataWeatherCity")
    fun getCountRecord() : Int

    @Query("select * from DataWeatherCity LIMIT 1")
    fun getTopRecord() : DataWeatherCity

    @Query("select*from DataWeatherCity where id = :id")
    fun getRecord(id : Int) : DataWeatherCity

    @Update
    suspend fun updateDataWeather(dataWeatherCity: DataWeatherCity)

    @Delete
    suspend fun deleteDataWeather(dataWeatherCity: DataWeatherCity)



}