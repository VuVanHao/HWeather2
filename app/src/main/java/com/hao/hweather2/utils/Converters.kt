package com.hao.hweather2.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson( value : List<com.hao.hweather2.model.List>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value : String ) = Gson().fromJson(value,Array<com.hao.hweather2.model.List>::class.java).toList()
}