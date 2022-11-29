package com.hao.hweather2.model

import androidx.room.Embedded

data class City(
    @Embedded
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)