package com.hao.hweather2.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import com.hao.hweather2.R
import com.hao.hweather2.adapter.WeatherDaysRCAdapter
import com.hao.hweather2.adapter.WeatherHoursRCAdapter
import com.hao.hweather2.databinding.FragmentCityPagerBinding
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.model.WeatherDays
import com.hao.hweather2.model.WeatherHours
import com.hao.hweather2.utils.MySharedPreferences
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.math.roundToLong

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CityPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CityPagerFragment : Fragment() {

    private var dataWeatherCity : DataWeatherCity? = null
    var countDataHours : Int = 1;
    var countData : Int = 0

    var weatherHoursRCAdapter : WeatherHoursRCAdapter? = null
    var weatherDaysRCAdapter : WeatherDaysRCAdapter ? = null

    private var arrayListWeatherHours : ArrayList<WeatherHours> = ArrayList()
    var arrayListWeatherDays : ArrayList<WeatherDays> = ArrayList()

    var dataTempHours : ArrayList<Entry> = ArrayList()
    var dataTempHoursHumidity : ArrayList<Entry> = ArrayList()
    var dataTempHoursCloud : ArrayList<Entry> = ArrayList()

    var dataTempMax : ArrayList<BarEntry> = ArrayList()
    var dataTempMin : ArrayList<BarEntry> = ArrayList()

    var weatherDays : WeatherDays ?= null
    var weatherHours : WeatherHours ?= null
    var state = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = Bundle()
        val gson = Gson()
        if (arguments != null)
        {
            val data : String = requireArguments().getString("CITY_DATA",null)
            dataWeatherCity = gson.fromJson(data,DataWeatherCity::class.java)
        }

    }
    private fun newInstance(dataWeatherCity: DataWeatherCity): CityPagerFragment {
        val args = Bundle()
        val gson = Gson()
        val data: String = gson.toJson(dataWeatherCity, DataWeatherCity::class.java)
        var cityPagerFragment = CityPagerFragment()
        args.putString("CITY_DATA", data)
        cityPagerFragment.arguments = args
        return cityPagerFragment
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context : Context = container!!.context
        val binding: FragmentCityPagerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_city_pager, container, false)
        val view : View = binding.root
        binding.dataWeatherCity = dataWeatherCity
        val icon = dataWeatherCity!!.list[2].weather[0].icon
        val iconImage : String = "https://openweathermap.org/img/wn/$icon.png"
        val imgWeather : ImageView = view.findViewById(R.id.imgWeather)
        Glide.with(context).load(iconImage).into(imgWeather)
        dataTempMin.clear()
        dataTempMax.clear()
        dataTempHours.clear()
        dataTempHoursHumidity.clear()
        dataTempHoursCloud.clear()
        arrayListWeatherHours.clear()
        arrayListWeatherDays.clear()
        initLang(view,context)
        getData(dataWeatherCity!!,context,view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun initLang(view: View, context: Context) {
        val tvnote : TextView = view.findViewById(R.id.tvnote)
        val tvdo1 : TextView = view.findViewById(R.id.tvdo1)
        val tvTypeDeg : TextView = view.findViewById(R.id.tvTypeDeg)
        val tv2 : TextView = view.findViewById(R.id.tv2)
        val tv3 : TextView = view.findViewById(R.id.tv3)
        val tv4 : TextView = view.findViewById(R.id.tv4)
        val tv5 : TextView = view.findViewById(R.id.tv5)
        val tv6 : TextView = view.findViewById(R.id.tv6)
        val tv7 : TextView = view.findViewById(R.id.tv7)
        val tv8 : TextView = view.findViewById(R.id.tv8)
        val tv9 : TextView = view.findViewById(R.id.tv9)
        val tv10 : TextView = view.findViewById(R.id.tv10)
        val tv11 : TextView = view.findViewById(R.id.tv11)
        val tv12 : TextView = view.findViewById(R.id.tv12)
        val tv13 : TextView = view.findViewById(R.id.tv13)
        val tv14 : TextView = view.findViewById(R.id.tv14)
        val relativeDetails : RelativeLayout = view.findViewById(R.id.details)
        val relativeabc : RelativeLayout = view.findViewById(R.id.abc);
        val hide : Button = view.findViewById(R.id.btnReadmore)
        tvnote.visibility = View.GONE
        relativeDetails.visibility = View.GONE
        relativeabc.visibility = View.GONE
        state = false
        val lang = MySharedPreferences.getLanguage(context)
        if (Objects.equals(lang,"vi"))
        {
            hide.text = "Thêm"
        }
        else
        {
            hide.text = "Read more"
        }
        hide.setOnClickListener {
            if (!state)
            {
                tvnote.visibility = View.VISIBLE
                relativeDetails.visibility = View.VISIBLE
                relativeabc.visibility = View.VISIBLE
                if (Objects.equals(lang,"vi"))
                {
                    hide.text = "Ẩn"
                }
                else
                {
                    hide.text = "Hide"
                }
                state = true
            }
            else
            {
                tvnote.visibility = View.GONE
                relativeDetails.visibility = View.GONE
                relativeabc.visibility = View.GONE
                val lang = MySharedPreferences.getLanguage(context)
                if (Objects.equals(lang,"vi"))
                {
                    hide.text = "Thêm"
                }
                else
                {
                    hide.text = "Read more"
                }
                state = false
            }
        }
        val unit = MySharedPreferences.getTempUnit(context)
        if (unit == 0)
        {
            tvTypeDeg.text = "C"
            tv2.text = "°C / "
            tv3.text = "°C"
        }
        else
        {
            tvTypeDeg.text = "F"
            tv2.text = "°F / "
            tv3.text = "°F"
        }
        if (Objects.equals(lang,"vi"))
        {
            tvdo1.text = "độ "
            tvnote.text = "Thông tin chi tiết"
            tv4.text = "Độ ẩm"
            tv5.text = "Áp suất"
            tv6.text = "Tốc độ gió"
            tv7.text = "Hướng gió"
            tv8.text = "Tầm nhìn"
            tv9.text = "Độ bao phủ"
            tv10.text = "Bình minh"
            tv11.text = "Hoàng hôn"
            tv12.text = "Lượng mưa"
            tv13.text = "Múi giờ"
            tv14.text = "Quốc gia"
        }
        else
        {
            tvdo1.text = "unit "
            tvnote.text = "Information Details"
            tv4.text = "Humidity"
            tv5.text = "Pressuare"
            tv6.text = "Wind Speed"
            tv7.text = "Wind Direction"
            tv8.text = "Visibility"
            tv9.text = "Cloud"
            tv10.text = "Sunrise"
            tv11.text = "Sunset"
            tv12.text = "Having Rain"
            tv13.text = "Timezone"
            tv14.text = "Country"
        }
    }


    private fun getData(dataWeatherCity: DataWeatherCity, context: Context, view: View) {

        val tvMaxTemp : TextView = view.findViewById(R.id.tvMaxTemp)
        val tvMinTemp : TextView = view.findViewById(R.id.tvMinTemp)
        val rvHours : RecyclerView = view.findViewById(R.id.rvTimeInDay)
        val rvDays : RecyclerView = view.findViewById(R.id.rvDayInWeek)
        val mpLineChart : LineChart = view.findViewById(R.id.line_chart_hours)
        val mpBarChart : BarChart = view.findViewById(R.id.line_chart_5days)

        //TODO getTempMin
        val unit = MySharedPreferences.getTempUnit(context)
        var tempMin  = dataWeatherCity.list[2].main.temp_min
        for (n in 3..9) {
            if (tempMin > dataWeatherCity.list[n].main.temp_min )
            {
                tempMin = dataWeatherCity.list[n].main.temp_min
            }
        }
        if (unit == 0) {
            tvMaxTemp.text = ((tempMin - 273).roundToLong().toString())
        } else {
            tvMaxTemp.text = ((1.8 * tempMin - 459.67).roundToLong().toString())
        }

        //TODO getTempMax

        var tempMax = dataWeatherCity.list[2].main.temp_max
        for (m in 3..9) {
            if (tempMax < dataWeatherCity.list[m].main.temp_max ) {
                tempMax = dataWeatherCity.list[m].main.temp_max
            }
        }
        if (unit == 0) {
            tvMinTemp.text = ((tempMax - 273).roundToLong().toString())
        } else {
            tvMinTemp.text = ((1.8 * tempMax - 459.67).roundToLong().toString())
        }

        //TODO recycleView Hours

        //Add value for Node Recycle and Node LineChart
        for(i in 3..10 )
        {
            weatherHours = WeatherHours()
            weatherHours!!.time = FormatTime.convertTime(dataWeatherCity.list[i].dt.toLong())
            weatherHours!!.imgWeather = dataWeatherCity.list[i].weather[0].icon
            weatherHours!!.humidity = dataWeatherCity.list[i].main.humidity
            weatherHours!!.temp = dataWeatherCity.list[i].main.temp
            arrayListWeatherHours.add(weatherHours!!)
            dataTempHours.add(Entry(countDataHours.toFloat(),FormatData.convertToStringTemp(weatherHours!!.temp!!,context).toFloat()))
            dataTempHoursHumidity.add(Entry(countDataHours.toFloat(),weatherHours!!.humidity!!.toFloat()))
            dataTempHoursCloud.add(Entry(countDataHours.toFloat(),dataWeatherCity.list[i].clouds.all.toFloat()))
            countDataHours++
        }
        weatherHoursRCAdapter = WeatherHoursRCAdapter(arrayListWeatherHours,context)
        rvHours.adapter = weatherHoursRCAdapter

        //TODO Chart

        var tempChart : String = ""
        var humidityChart : String = ""
        var cloud : String = ""
        val langChart : String = MySharedPreferences.getLanguage(context)
        if (Objects.equals(langChart,"vi"))
        {
            tempChart = "Nhiệt độ"
            humidityChart = "Độ ẩm"
            cloud = "Độ bao phủ"
        }
        else
        {
            tempChart = "Temp"
            humidityChart = "Humidity"
            cloud = "Cloud"
        }

        val lineDataSet = LineDataSet(dataTempHours,tempChart)
        val lineDataSet1  = LineDataSet(dataTempHoursHumidity,humidityChart)
        val lineDataSet2  = LineDataSet(dataTempHoursCloud,cloud)

        val dataSets : ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)
        dataSets.add(lineDataSet1)
        dataSets.add(lineDataSet2)

        mpLineChart.setNoDataText("No Data")
        mpLineChart.setNoDataTextColor(Color.RED)


        //Description
        val description1 : Description = Description()
        description1.text = ""
        description1.textColor = Color.BLUE
        description1.textSize = 15f
        mpLineChart.description = description1

        //Style Border
        mpLineChart.setDrawBorders(true)
        mpLineChart.setBorderColor(Color.RED)
        mpLineChart.setBorderWidth(1f)

        //set line
        lineDataSet.lineWidth = 2f
        lineDataSet1.color = Color.BLUE
        lineDataSet.color = Color.RED
        lineDataSet2.color = Color.BLACK

        val hours = arrayOf("",
            arrayListWeatherHours[0].time,
            arrayListWeatherHours[1].time,
            arrayListWeatherHours[2].time,
            arrayListWeatherHours[3].time,
            arrayListWeatherHours[4].time,
            arrayListWeatherHours[5].time,
            arrayListWeatherHours[6].time,
            arrayListWeatherHours[7].time,
        )

        val xAxis : XAxis = mpLineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(hours)
        xAxis.setCenterAxisLabels(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM


        val dataHours = LineData(dataSets)
        mpLineChart.data = dataHours
        mpLineChart.invalidate()

        //TODO Weather next 5days

        var position : Int = 0
        for (i in 0 .. 30)
        {
            if (Objects.equals(FormatTime.convertTime(dataWeatherCity.list[i].dt.toLong()),"12:00"))
            {
                position = i
                break
            }
        }
        for (j in position..39 step 8)
        {
            weatherDays = WeatherDays()
            if (j < 2)
            {
                weatherDays!!.day = convertDay(dataWeatherCity.list[j].dt.toLong())
                weatherDays!!.description = dataWeatherCity.list[j].weather[0].description
                if (unit == 0)
                {
                    weatherDays!!.tempMin = ((dataWeatherCity.list[0].main.temp_min - 273).toInt()).toDouble()
                }
                else
                {
                    weatherDays!!.tempMin = ((1.8 * dataWeatherCity.list[0].main.temp_min - 459).toInt()).toDouble()
                }
            }
            else
            {
                weatherDays!!.day = convertDay(dataWeatherCity.list[j-2].dt.toLong())
                weatherDays!!.description = dataWeatherCity.list[j].weather[0].description
                if (unit == 0)
                {
                    weatherDays!!.tempMin = ((dataWeatherCity.list[j-2].main.temp_min - 273).toInt()).toDouble()
                }
                else
                {
                    weatherDays!!.tempMin = ((1.8 * dataWeatherCity.list[j-2].main.temp_min - 459).toInt()).toDouble()
                }
            }
            if (unit == 0)
            {
                weatherDays!!.tempMax = ((dataWeatherCity.list[j].main.temp_max - 273).toInt()).toDouble()
            }
            else
            {
                weatherDays!!.tempMax = ((1.8 * dataWeatherCity.list[j].main.temp_max - 459).toInt()).toDouble()
            }
            arrayListWeatherDays.add(weatherDays!!)
            val icon = dataWeatherCity.list[j].weather[0].icon
            val iconImage : String = "https://openweathermap.org/img/wn/$icon.png"
            weatherDays!!.imgWeatherDay = iconImage
            dataTempMin.add(BarEntry(countData.toFloat(),weatherDays?.tempMin!!.toFloat()))
            dataTempMax.add(BarEntry(countData.toFloat(),weatherDays?.tempMax!!.toFloat()))
            countData++
        }

        if (Objects.equals(langChart,"vi"))
        {
            arrayListWeatherDays[0].day = "Hôm nay"
            arrayListWeatherDays[1].day = "Ngày mai"
        }
        else
        {
            arrayListWeatherDays[0].day = "Today"
            arrayListWeatherDays[1].day = "Tomorrow"
        }

        weatherDaysRCAdapter = WeatherDaysRCAdapter(arrayListWeatherDays,context)
        rvDays.adapter = weatherDaysRCAdapter


        //TODO BarChart

        var desMin = ""
        var desMax = ""
        var desChart = ""
        if (Objects.equals(langChart,"vi"))
        {
            desMin = "Nhiệt độ thấp nhất"
            desMax = "Nhiệt độ cao nhất"
            desChart = "Biểu đồ nhiệt độ 5 ngày tới"
        }
        else
        {
            desMin = "Minimum temperature"
            desMax = "Maximum temperature"
            desChart = "TempChart for the next 5 days"
        }

        val barDataSet1 = BarDataSet(dataTempMax,desMax)
        barDataSet1.color = Color.RED
        val barDataSet2 = BarDataSet(dataTempMin,desMin)
        barDataSet2.color = Color.BLACK


        val data = BarData(barDataSet1,barDataSet2)

        val description = Description()
        description.text = desChart
        description.textColor = Color.BLUE
        description.textSize = 15f
        mpBarChart.description = description

        mpBarChart.data = data

        val days = arrayOf(
            arrayListWeatherDays[0].day,
            arrayListWeatherDays[1].day,
            arrayListWeatherDays[2].day,
            arrayListWeatherDays[3].day,
            arrayListWeatherDays[4].day)


        //TODO set Properties for barchart
        val xAxis1 = mpBarChart.xAxis
        xAxis1.valueFormatter = IndexAxisValueFormatter(days)
        xAxis1.setCenterAxisLabels(true)
        xAxis1.position = XAxis.XAxisPosition.BOTTOM
        xAxis1.granularity = 1f
        xAxis1.isGranularityEnabled = true

        mpBarChart.isDragEnabled = true
        mpBarChart.setVisibleXRangeMaximum(3f)

        val barSpace = 0.08f
        val groupSpace = 0.45f
        data.barWidth = 0.2f

        mpBarChart.xAxis.axisMinimum = 0f
        mpBarChart.xAxis.axisMaximum = 0f + mpBarChart.barData.getGroupWidth(groupSpace,barSpace) * 7
        mpBarChart.axisLeft.axisMinimum = 0f

        mpBarChart.groupBars(0f,groupSpace,barSpace)

        mpBarChart.invalidate()


    }


    private fun convertDay(input : Long) : String
    {
        var formattedDtm :String = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("E")
            formattedDtm = Instant.ofEpochSecond(input)
                .atZone(ZoneId.of("GMT-4"))
                .format(formatter)
        }
        return formattedDtm
    }

    companion object {


        fun newInstance(dataWeatherCity: DataWeatherCity): CityPagerFragment {
            val args = Bundle()
            val gson = Gson()
            val data: String = gson.toJson(dataWeatherCity, DataWeatherCity::class.java)
            val cityPagerFragment = CityPagerFragment()
            args.putString("CITY_DATA", data)
            cityPagerFragment.arguments = args
            return cityPagerFragment
        }
    }



    object FormatData {
        @JvmStatic
        fun convertToStringTemp(input: Double, context: Context): String {
            val unit: Int = MySharedPreferences.getTempUnit(context)
            val output: Double
            if (unit == 0)
            {
                output = ((input - 273).toInt()).toDouble()
            }
            else
            {
                output = ((1.8 * input - 459.67).toInt()).toDouble()
            }
            return output.toInt().toString()
        }
        @JvmStatic
        fun convertToString(input: Double): String {
            return input.toString() + ""
        }
        @JvmStatic
        fun convertNameCity(name: String, country: String): String {
            return "$name, $country"
        }
    }

    object FormatTime{

        @JvmStatic
        fun convertTime(input : Long) : String
        {
            var formattedDtm :String = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                formattedDtm = Instant.ofEpochSecond(input)
                    .atZone(ZoneId.of("GMT-4"))
                    .format(formatter)
            }
            return formattedDtm
        }
        @JvmStatic
        fun convertDayTime() : String
        {
            var dtf : DateTimeFormatter? = null
            val now : LocalDateTime
            var current = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                dtf = DateTimeFormatter.ofPattern("E, dd/MM/yyyy")
                now = LocalDateTime.now()
                current = dtf.format(now)
            }
            return current
        }
    }


}