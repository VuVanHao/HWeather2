package com.hao.hweather2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hao.hweather2.adapter.CityWeatherViewPager
import com.hao.hweather2.fragment.CityMangeFragment
import com.hao.hweather2.fragment.DetailsWeatherFragment
import com.hao.hweather2.fragment.QuickSearchFragment
import com.hao.hweather2.fragment.SettingFragment
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.utils.MySharedPreferences
import com.hao.hweather2.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal
import java.io.IOException
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private var cityWeatherViewPager: CityWeatherViewPager? = null

    private var lat = ""
    private var lon = ""

    private var geocoder: Geocoder? = null

    private var cityMangeFragment: CityMangeFragment? = null
    private var settingFragment: SettingFragment? = null
    private var quickSearchFragment: QuickSearchFragment? = null
    private var detailsWeatherFragment: DetailsWeatherFragment? = null
    var stateAddFragment = false
    var stateReload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appComponent = (application as FakerApplication).applicationComponent
        appComponent.inject(this)

        geocoder = Geocoder(this)
        tapTargetFirstTime()
        checkPermissionAgain()
        imgHomePage.setBackgroundColor(Color.GRAY)
        if (checkNetwork()) {
            addPosCurrent(mainViewModel)
            updateListWeather(mainViewModel)

            mainViewModel.getAllWeather().observe(this) {
                val listDataWeatherCity: List<DataWeatherCity> = it
                cityWeatherViewPager =
                    CityWeatherViewPager(supportFragmentManager, listDataWeatherCity)
                vpCity.adapter = cityWeatherViewPager
                circle_indicator.setViewPager(vpCity)
                cityWeatherViewPager!!.registerDataSetObserver(circle_indicator.dataSetObserver)
                cityWeatherViewPager!!.notifyDataSetChanged()
            }
        } else {
            mainViewModel.getAllWeather().observe(this) {
                val listDataWeatherCity: List<DataWeatherCity> = it
                cityWeatherViewPager =
                    CityWeatherViewPager(supportFragmentManager, listDataWeatherCity)
                vpCity.adapter = cityWeatherViewPager
                circle_indicator.setViewPager(vpCity)
                cityWeatherViewPager!!.registerDataSetObserver(circle_indicator.dataSetObserver)
                cityWeatherViewPager!!.notifyDataSetChanged()
            }
        }
        buttonFunc()


    }

    private fun buttonFunc() {
        val imgManageCity: ImageView = findViewById(R.id.imgManageCity)
        imgManageCity.setOnClickListener {
            cityMangeFragment = CityMangeFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fg_contentOther, cityMangeFragment!!)
                .commit()
            stateAddFragment = true
            selectButton(3)
        }

        val imgHomePage: ImageView = findViewById(R.id.imgHomePage)
        imgHomePage.setOnClickListener {
            if (stateAddFragment) {
                if (cityMangeFragment != null) {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(cityMangeFragment!!).commit()
                }

                if (settingFragment != null) {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(settingFragment!!).commit()

                    if (stateReload) {
                        startActivity(Intent(this, MainActivity::class.java))
                        stateReload = false
                    }

                }

                if (quickSearchFragment != null) {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(quickSearchFragment!!).commit()
                }

                if (detailsWeatherFragment != null) {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(detailsWeatherFragment!!).commit()
                }

                stateAddFragment = false
            }
            selectButton(2)
        }

        val imgSetting: ImageView = findViewById(R.id.imgSetting)
        imgSetting.setOnClickListener {
            if (stateAddFragment) {
                settingFragment = SettingFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fg_contentOther, settingFragment!!).commit()
            } else {
                settingFragment = SettingFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fg_contentOther, settingFragment!!).commit()
                stateAddFragment = true
            }
            selectButton(1)
        }

        val imgSearchMap: ImageView = findViewById(R.id.imgSearchMap)
        imgSearchMap.setOnClickListener {
            startActivity(Intent(this, SearchMapActivity::class.java))
        }


        val imgSearch: ImageView = findViewById(R.id.imgSearch)
        imgSearch.setOnClickListener {
            if (stateAddFragment) {
                quickSearchFragment = QuickSearchFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fg_contentOther, quickSearchFragment!!).commit()
            } else {
                quickSearchFragment = QuickSearchFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fg_contentOther, quickSearchFragment!!).commit()
                stateAddFragment = true
            }
            selectButton(4)
        }
    }

    private fun tapTargetFirstTime() {
        if (MySharedPreferences.getFirstLaunch(this) == 0) {
            MySharedPreferences.setFirsLaunch(this, 1)
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(imgSetting)
                .setPrimaryText("Đây là cài đặt")
                .setSecondaryText("Bấm vào đây để chỉnh sửa các cài đặt cho ứng dụng của bạn.")
                .setPromptFocal(RectanglePromptFocal())
                .setPromptStateChangeListener { _, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        goto2()
                    }
                }
                .show()
        }
    }

    private fun goto2() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(imgHomePage)
            .setPrimaryText("Trang chủ")
            .setSecondaryText("Hiển thị các thông tin chi tiết về các địa điểm. Được mở mặc định khi mở ứng dụng")
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    goto3()
                }
            }
            .show()
    }

    private fun goto3() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(imgSearchMap)
            .setPrimaryText("Tìm kiếm thời tiết bằng bản đồ")
            .setSecondaryText("Bấm vào để tìm kiếm thời tiết các khu vực trên bản đồ.")
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    goto4()
                }
            }
            .show()
    }

    private fun goto4() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(imgManageCity)
            .setPrimaryText("Quản lí thành phố")
            .setSecondaryText("Bấm vào để xem danh sách và quản lí các thành phố của bạn")
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                    goto5()
                }
            }
            .show()
    }

    private fun goto5() {
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(imgSearch)
            .setPrimaryText("Tìm kiếm nhanh")
            .setSecondaryText("Bấm vào để tìm kiếm tiết dựa trên tên thành phố")
            .setPromptFocal(RectanglePromptFocal())
            .setPromptStateChangeListener { _, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                }
            }
            .show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateListWeather(mainViewModel: MainViewModel) {
        val list = mainViewModel.getAlRecord()
        GlobalScope.launch {
            for (i in 0 until list.size - 1) {
                val dataWeatherCityTop = mainViewModel.getRecord(list[i].city.id)
                val it = mainViewModel.getInforWeather(list[i].city.name, "vi")
                dataWeatherCityTop.message = it.message
                dataWeatherCityTop.city = it.city
                dataWeatherCityTop.cnt = it.cnt
                dataWeatherCityTop.list = it.list
                dataWeatherCityTop.cod = it.cod
                mainViewModel.updateWeather(dataWeatherCityTop)
            }
        }
    }


    fun getPosition() {
        get_GPs()
        if (!Objects.equals(lat, "")) {
            var name = getNameCity(lat, lon)
            if (name.length > 1) {
                if (Objects.equals(name, "Ha Tay")) {
                    name = "hà nội"
                }
                mainViewModel.getOneWeather(name, "vi")
                mainViewModel.dataWeatherCity.observe(this) {
                    val dataWeatherCityTop = mainViewModel.getTopRecord()
                    dataWeatherCityTop.message = it.message
                    dataWeatherCityTop.city = it.city
                    dataWeatherCityTop.cnt = it.cnt
                    dataWeatherCityTop.list = it.list
                    dataWeatherCityTop.cod = it.cod
                    mainViewModel.updateWeather(dataWeatherCityTop)
                }
                Toast.makeText(this, "Đã cập nhật lại vị trí !!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onRestart() {
        super.onRestart()
        getPosition()
    }

    private fun addPosCurrent(mainViewModel: MainViewModel) {
        val count = mainViewModel.getCountRecord()
        if (count == 0) {
            mainViewModel.insertWeather("hà nội", "vi")
        } else {
            get_GPs()
            if (!Objects.equals(lat, "")) {
                var name = getNameCity(lat, lon)
                if (name.length > 1) {
                    if (Objects.equals(name, "Ha Tay")) {
                        name = "hà nội"
                    }
                    mainViewModel.getOneWeather(name, "vi")
                    mainViewModel.dataWeatherCity.observe(this) {
                        val dataWeatherCityTop = mainViewModel.getTopRecord()
                        dataWeatherCityTop.message = it.message
                        dataWeatherCityTop.city = it.city
                        dataWeatherCityTop.cnt = it.cnt
                        dataWeatherCityTop.list = it.list
                        dataWeatherCityTop.cod = it.cod
                        mainViewModel.updateWeather(dataWeatherCityTop)
                    }

                    Toast.makeText(this, "Đã cập nhật lại vị trí !!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun get_GPs() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                99
            )
        } else {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                lat = location.latitude.toString() + ""
                lon = location.longitude.toString() + ""
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 0f
            ) { location ->
                lat = location.latitude.toString() + ""
                lon = location.longitude.toString() + ""
            }
        }
    }

    fun getNameCity(lat: String, lon: String): String {
        var nameCity = ""
        var addresses: List<Address> = ArrayList()
        try {
            if (!Objects.equals(lat, "") && !Objects.equals(lon, "")) {
                addresses = geocoder!!.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (addresses.size > 0) {
            nameCity = addresses[0].adminArea
        }
        return nameCity
    }

    private fun checkNetwork(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val dataMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return wifi!!.isConnected || dataMobile!!.isConnected
    }

    fun checkPermissionAgain() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                99
            )
        }
    }

    fun removeCityFragment(pos: Int) {
        if (cityMangeFragment != null) {
            supportFragmentManager
                .beginTransaction()
                .remove(cityMangeFragment!!).commit()

            stateAddFragment = false
        }
        vpCity.currentItem = pos;
        selectButton(2)

    }

    fun reloadFragmentSetting() {
        if (settingFragment != null) {
            settingFragment = SettingFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fg_contentOther, settingFragment!!).commit()

            stateReload = true;
        }
    }

    fun goToSeachFragment(nameCity: String) {
        detailsWeatherFragment = DetailsWeatherFragment.newInstance(nameCity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fg_contentOther, detailsWeatherFragment!!).commit()

        stateAddFragment = true
        selectButton(4)
    }

    fun selectButton(stt: Int) {
        if (stt == 1) {
            imgSetting.setBackgroundColor(Color.DKGRAY)
            imgHomePage.setBackgroundColor(Color.WHITE)
            imgManageCity.setBackgroundColor(Color.WHITE)
            imgSearch.setBackgroundColor(Color.WHITE)
        } else if (stt == 2) {
            imgSetting.setBackgroundColor(Color.WHITE)
            imgHomePage.setBackgroundColor(Color.DKGRAY)
            imgManageCity.setBackgroundColor(Color.WHITE)
            imgSearch.setBackgroundColor(Color.WHITE)
        } else if (stt == 3) {
            imgSetting.setBackgroundColor(Color.WHITE)
            imgHomePage.setBackgroundColor(Color.WHITE)
            imgManageCity.setBackgroundColor(Color.DKGRAY)
            imgSearch.setBackgroundColor(Color.WHITE)
        } else {
            imgSetting.setBackgroundColor(Color.WHITE)
            imgHomePage.setBackgroundColor(Color.WHITE)
            imgManageCity.setBackgroundColor(Color.WHITE)
            imgSearch.setBackgroundColor(Color.DKGRAY)
        }
    }

}