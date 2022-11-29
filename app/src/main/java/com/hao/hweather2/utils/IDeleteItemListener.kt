package com.hao.hweather2.utils

import com.hao.hweather2.model.DataWeatherCity

interface IDeleteItemListener {
    fun iDeteleItem(dataWeatherCity: DataWeatherCity)
    fun iReadMoreItem(i: Int)
    fun iDelItemName(name: String?)
    fun iDelItemUnCheck(dataWeatherCity: DataWeatherCity)
}