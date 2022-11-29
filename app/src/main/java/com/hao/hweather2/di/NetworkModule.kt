package com.hao.hweather2.di

import com.hao.hweather2.retrofit.IWeatherServices
import com.hao.hweather2.utils.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit
    {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesIWeatherServices( retrofit: Retrofit) : IWeatherServices
    {
        return retrofit.create(IWeatherServices::class.java)
    }
}