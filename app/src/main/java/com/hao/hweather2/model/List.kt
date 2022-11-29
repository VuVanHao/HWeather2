package com.hao.hweather2.model

import kotlin.collections.List

data class List (
    val dt:Int,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Float,
    val rain: Rain,
    val sys: Sys,
    val dtTxt: String
    )