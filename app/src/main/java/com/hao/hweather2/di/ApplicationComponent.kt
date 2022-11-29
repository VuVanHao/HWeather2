package com.hao.hweather2.di

import android.content.Context
import com.hao.hweather2.InforCityMapActivity
import com.hao.hweather2.MainActivity
import com.hao.hweather2.WeatherWidget
import com.hao.hweather2.fragment.CityMangeFragment
import com.hao.hweather2.fragment.DetailsWeatherFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class,DatabaseModule::class]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(cityMangeFragment: CityMangeFragment)
    fun inject(inforCityMapActivity: InforCityMapActivity)
    fun inject(detailsWeatherFragment: DetailsWeatherFragment)
    fun inject(weatherWidget: WeatherWidget)

    @Component.Factory
    interface Factory
    {
        fun create(@BindsInstance context: Context) : ApplicationComponent
    }



}