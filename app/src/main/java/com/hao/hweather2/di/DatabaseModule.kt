package com.hao.hweather2.di

import android.content.Context
import androidx.room.Room
import com.hao.hweather2.database.FakerDatabase
import com.hao.hweather2.database.WeatherDAO
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideWeatherDB(context : Context) : FakerDatabase
    {
        return Room.databaseBuilder(context,FakerDatabase::class.java,"WeatherDB").allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }


    @Singleton
    @Provides
    fun provideWeatherDAO(fakerDatabase: FakerDatabase) : WeatherDAO
    {
        return fakerDatabase.getWeatherDAO()
    }
}