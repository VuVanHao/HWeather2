package com.hao.hweather2.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hao.hweather2.R

class SearchListCityAdapter(var context: Context, var listCity : List<String>) : BaseAdapter() {
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
            view = View.inflate(p2!!.context, R.layout.custom_list_city_search,null)
        }
        val nameCity = listCity[p0]
        val name = view!!.findViewById<TextView>(R.id.tvName)
        name.text = nameCity
        return view
    }

}