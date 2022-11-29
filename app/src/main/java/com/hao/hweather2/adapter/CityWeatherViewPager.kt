package com.hao.hweather2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.hao.hweather2.fragment.CityPagerFragment
import com.hao.hweather2.model.DataWeatherCity

class CityWeatherViewPager(var fm : FragmentManager,var dataWeatherCity:List<DataWeatherCity>) : FragmentStatePagerAdapter(fm) {

    private var fragmentCities : ArrayList<CityPagerFragment>? = null

    init {
        fragmentCities = ArrayList<CityPagerFragment>()
        for (i in dataWeatherCity.indices) {
            var city : CityPagerFragment = CityPagerFragment.newInstance(dataWeatherCity[i])
            fragmentCities!!.add(city)
        }
    }

    override fun getCount(): Int {
        return fragmentCities!!.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentCities!!.get(position)
    }
}