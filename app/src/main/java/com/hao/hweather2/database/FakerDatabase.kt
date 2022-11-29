package com.hao.hweather2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.utils.Converters

@Database(
    entities = [DataWeatherCity::class], version = 1
)
@TypeConverters(Converters::class)
abstract class FakerDatabase : RoomDatabase() {

    abstract fun getWeatherDAO() : WeatherDAO
}