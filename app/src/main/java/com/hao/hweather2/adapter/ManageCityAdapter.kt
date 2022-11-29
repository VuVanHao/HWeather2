package com.hao.hweather2.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hao.hweather2.R
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.utils.IDeleteItemListener
import com.hao.hweather2.utils.MySharedPreferences


class ManageCityAdapter(var context: Context, var listCity : List<DataWeatherCity>, var iDeleteItemListener: IDeleteItemListener) : BaseAdapter() {

    var activate = false

    override fun getCount(): Int {
        return listCity.size
    }

    override fun getItem(p0: Int): Any {
        return listCity[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1
        if (view == null)
        {
            view = View.inflate(p2!!.context,R.layout.custom_manage_listview,null)
        }
        val dataWeatherCity = listCity[p0]
        val nameCity = view!!.findViewById<TextView>(R.id.tvNameCityManage)
        val Temp = view.findViewById<TextView>(R.id.tvTempCurrentManage)
        val desc = view.findViewById<TextView>(R.id.tvDescManage)
        val imgDes = view.findViewById<ImageView>(R.id.imgDes)
        val tempMin = view.findViewById<TextView>(R.id.tvMinTempManage)
        val tempMax = view.findViewById<TextView>(R.id.tvMaxTempManage)
        val tvModeTemp = view.findViewById<TextView>(R.id.tvModeTemp)
        val cbDelItem = view.findViewById<CheckBox>(R.id.cbDelItem)
        val tv2 = view.findViewById<TextView>(R.id.tv2)
        val tv3 = view.findViewById<TextView>(R.id.tv3)
        val lang = MySharedPreferences.getLanguage(context)
        val unit = MySharedPreferences.getTempUnit(context)
        if (unit == 0)
        {
            tvModeTemp.text = "°C"
            tv2.text = "°C / "
            tv3.text = "°C"
        } else {
            tvModeTemp.text = "°F"
            tv2.text = "°F / "
            tv3.text = "°F"
        }
        nameCity.text = dataWeatherCity.city.name
        desc.text = dataWeatherCity.list[2].weather[0].description
        val icon = dataWeatherCity.list[2].weather[0].icon
        val iconImage : String = "https://openweathermap.org/img/wn/$icon.png"
        Glide.with(context).load(iconImage).into(imgDes)
        //TODO getTempMin
        var tempMinManage: Double = dataWeatherCity.list[2].main.temp_min
        for (n in 3..9) {
            if (tempMinManage > dataWeatherCity.list[n].main.temp_min) {
                tempMinManage = dataWeatherCity.list[n].main.temp_min
            }
        }
        //TODO getTempMax
        var tempMaxManage: Double = dataWeatherCity.list[2].main.temp_max
        for (m in 3..9) {
            if (tempMaxManage < dataWeatherCity.list[m].main.temp_max) {
                tempMaxManage = dataWeatherCity.list[m].main.temp_max
            }
        }

        if (unit == 0)
        {
            tempMin.text = ((tempMinManage - 273).toInt()).toString()
            tempMax.text = ((tempMaxManage - 273).toInt()).toString()
            Temp.text = ((dataWeatherCity.list[0].main.temp - 273).toInt()).toString()
        }
        else
        {
            tempMin.text = ((1.8 * tempMinManage - 459.67).toInt()).toString()
            tempMax.text = ((1.8 * tempMaxManage - 459.67).toInt()).toString()
            Temp.text = ((1.8 * dataWeatherCity.list[0].main.temp - 459.67).toInt()).toString()
        }
        cbDelItem.setOnCheckedChangeListener { _, b ->
            if (b) {
                iDeleteItemListener.iDeteleItem(dataWeatherCity)
            }
            if (!b) {
                iDeleteItemListener.iDelItemUnCheck(dataWeatherCity)
            }
        }

        if (activate) {
            if (p0 == 0 )
            {
                cbDelItem.visibility = View.INVISIBLE
            }else
            {
                cbDelItem.visibility = View.VISIBLE
            }
            tvModeTemp.setPadding(0,0,150,0);
            desc.setPadding(0,0,150,0);
        }
        else
        {
            cbDelItem.isChecked = false;
            cbDelItem.visibility = View.INVISIBLE;
            tvModeTemp.setPadding(0,0,0,0);
            Temp.setPadding(150,0,0,0);
            desc.setPadding(0,0,0,0);
        }

        return view
    }

    fun activateButtons(activate: Boolean) {
        this.activate = activate
        notifyDataSetChanged() //need to call it for the child views to be re-created with buttons.
    }
}