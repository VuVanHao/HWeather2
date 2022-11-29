package com.hao.hweather2.retrofit

import android.telecom.Call
import com.hao.hweather2.model.DataWeatherCity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherServices {

    @GET("forecast?")
    suspend fun getWeatherByNameCity(
        @Query("q") cityName: String?, @Query("lang") lang: String?,
        @Query("appid") apiKey: String?
    ): Response<DataWeatherCity>

    @GET("forecast?")
    suspend fun getWeatherByLocation(
        @Query("lat") lat: String?, @Query("lon") lon: String?,
        @Query("lang") lang: String?,
        @Query("appid") apiKey: String?
    ):Response<DataWeatherCity>
}