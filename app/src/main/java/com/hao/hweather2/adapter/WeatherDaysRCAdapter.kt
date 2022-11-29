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
import com.hao.hweather2.model.WeatherDays
import com.hao.hweather2.utils.MySharedPreferences

class WeatherDaysRCAdapter(var listWeatherDay : ArrayList<WeatherDays>, val context: Context) : RecyclerView.Adapter<WeatherDaysRCAdapter.WeatherDaysViewHolder>() {

    class WeatherDaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var day : TextView = itemView.findViewById(R.id.tvday)
        var descriptionday : TextView = itemView.findViewById(R.id.tvDescriptionDay)
        var tempMin : TextView = itemView.findViewById(R.id.tvMinTempDay)
        var tempMax : TextView = itemView.findViewById(R.id.tvMaxTempDay)
        var imgWeatherDay : ImageView = itemView.findViewById(R.id.imgWeatherDay)
        var tv2 : TextView = itemView.findViewById(R.id.tv2)
        var tv3 : TextView = itemView.findViewById(R.id.tv3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDaysViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_weatherdays,parent,false)
        return WeatherDaysViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherDaysViewHolder, position: Int) {
        val weatherDay : WeatherDays = listWeatherDay[position]
        Glide.with(context).load(weatherDay.imgWeatherDay).into(holder.imgWeatherDay)
        holder.day.text = weatherDay.day
        holder.descriptionday.text = weatherDay.description
        holder.tempMin.text = weatherDay.tempMin.toString()
        holder.tempMax.text = weatherDay.tempMax.toString()
        val unit = MySharedPreferences.getTempUnit(context)
        if (unit == 0) {
            holder.tv2.text = "°C / "
            holder.tv3.text = "°C"
        }
        else
        {
            holder.tv2.text = "°F / "
            holder.tv3.text = "°F"
        }
    }

    override fun getItemCount(): Int {
       return listWeatherDay.size
    }
}