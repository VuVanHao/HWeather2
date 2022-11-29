package com.hao.hweather2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hao.hweather2.R
import com.hao.hweather2.model.WeatherHours
import com.hao.hweather2.utils.MySharedPreferences
import kotlin.math.roundToLong

class WeatherHoursRCAdapter(var listWeather : ArrayList<WeatherHours>, val context: Context)
    : RecyclerView.Adapter<WeatherHoursRCAdapter.WeatherHoursViewHolder>(){

    class WeatherHoursViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val time  : TextView = itemView.findViewById(R.id.tvHours)
        val humidity : TextView = itemView.findViewById(R.id.humidity)
        val temp : TextView = itemView.findViewById(R.id.tvTempcurrent)
        val imgWeather: ImageView = itemView.findViewById(R.id.imgWeatherHours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHoursViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_weatherhours,parent,false)
        return WeatherHoursViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherHoursViewHolder, position: Int) {
        val weather : WeatherHours = listWeather.get(position)
        var icon = weather.imgWeather
        var iconImage : String = "https://openweathermap.org/img/wn/$icon.png"
        Glide.with(context).load(iconImage).into(holder.imgWeather)
        holder.time.text = weather.time
        holder.humidity.text = weather.humidity.toString()
        val unit = MySharedPreferences.getTempUnit(context)
        if (unit == 0)
        {
            holder.temp.text = (weather.temp?.minus(273)!!.toInt()).toString()
        }
        else
        {
            holder.temp.text = ((1.8 * weather.temp!!.toInt() - 459.67).toInt()).toString()
        }
    }

    override fun getItemCount(): Int {
        return listWeather.size
    }


}