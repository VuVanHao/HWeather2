package com.hao.hweather2.di

import android.content.Context
import androidx.room.Room
import com.hao.hweather2.database.FakerDatabase
import com.hao.hweather2.database.WeatherDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideWeatherDB(@ApplicationContext context : Context) : FakerDatabase
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