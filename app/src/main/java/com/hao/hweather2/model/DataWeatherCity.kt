package com.hao.hweather2.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.collections.List

@Entity
data class DataWeatherCity(
    @Embedded
    var city: City,
    var cnt: Int,
    var cod: String,
    var list: List<com.hao.hweather2.model.List>,
    var message: Int
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_weather")
    var id: Int? = null
}