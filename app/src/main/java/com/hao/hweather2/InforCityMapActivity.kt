package com.hao.hweather2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.hao.hweather2.databinding.ActivityInforCityMapBinding
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.utils.MySharedPreferences
import com.hao.hweather2.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_infor_city_map.*
import java.util.*
import javax.inject.Inject

class InforCityMapActivity : AppCompatActivity() {

    
    var lat = ""
    var lon = ""
    var lang = ""
    var binding : ActivityInforCityMapBinding ? = null
    var dataWeatherCity : DataWeatherCity ? = null

    @Inject
    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infor_city_map)
        
        val appComponent = (application as FakerApplication).applicationComponent
        appComponent.inject(this)
        
        binding = DataBindingUtil.setContentView(this,R.layout.activity_infor_city_map)
        lang = MySharedPreferences.getLanguage(this)
        initLangUnitTemp()
        lat = intent.getStringExtra("LAT").toString()
        lon = intent.getStringExtra("LONG").toString()
        tvLat.text = lat
        tvLong.text = lon
        initData(binding,mainViewModel)
        backButton()
    }

    private fun initData(binding: ActivityInforCityMapBinding?, mainViewModel: MainViewModel) {

        mainViewModel.getWeatherLocation(lat,lon,lang)
        mainViewModel.dataWeatherCity_Location.observe(this) {
            binding?.dataWeatherCity = it
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initLangUnitTemp() {
        if (Objects.equals(lang,"vi"))
        {
            tv11.text = "Vĩ độ: "
            tv22.text = "Kinh độ: "
            tvactionbarSearch.text = "Thông tin thời tiết : "
            tv1.text = "Nhiệt độ hiện tại : "
            tv4.text = "Độ ẩm"
            tv5.text = "Áp suất"
            tv6.text = "Tốc độ gió"
            tv7.text = "Hướng gió"
            tv8.text = "Tầm nhìn"
            tv9.text = "Độ bao phủ"
            tv12.text = "Lượng mưa"
        }
        else
        {
            tv11.text = "Latitude: ";
            tv22.text = "Longitude: ";
            tvactionbarSearch.text = "Information";
            tv1.text = "Current temp : ";
            tv4.text = "Humidity";
            tv5.text = "Pressuare";
            tv6.text = "Wind Speed";
            tv7.text = "Wind Direction";
            tv8.text = "Visibility";
            tv9.text = "Cloud";
            tv12.text = "Having Rain";
        }

        val unit = MySharedPreferences.getTempUnit(this)
        if (unit == 0)
        {
            tvTempMode.text = "°C"
        }
        else
        {
            tvTempMode.text = "°F"
        }
    }

    private fun backButton() {
        btnBackWeatherDetail.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this,SearchMapActivity::class.java))
    }
}