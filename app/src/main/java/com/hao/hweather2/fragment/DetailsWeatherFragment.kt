package com.hao.hweather2.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.hao.hweather2.MainActivity
import com.hao.hweather2.R
import com.hao.hweather2.databinding.FragmentDetailsWeatherBinding
import com.hao.hweather2.model.DataWeatherCity
import com.hao.hweather2.utils.MySharedPreferences
import com.hao.hweather2.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailsWeatherFragment : Fragment() {

    var nameCity  = ""
    var tv1 : TextView ?= null
    var tv4 : TextView ?= null
    var tv5 : TextView ?= null
    var tv6 : TextView ?= null
    var tv7 : TextView ?= null
    var tv8 : TextView ?= null
    var tv9 : TextView ?= null
    var tv12 : TextView ?= null
    var tvTempMode : TextView ? = null

    var titleDetails : RelativeLayout ? = null
    var details : RelativeLayout ? = null
    var ln1 : LinearLayout ? = null
    var processBar : ProgressBar ? = null

    var lang = ""
    var unit = 0


    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        {
            nameCity = requireArguments().getString("NAME_DATA",null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = container!!.context

        val binding : FragmentDetailsWeatherBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details_weather, container, false)
        lang = MySharedPreferences.getLanguage(context)
        unit = MySharedPreferences.getTempUnit(context)
        val view = binding.root

        initLangTemp(context,view)
        titleDetails!!.visibility = View.INVISIBLE;
        details!!.visibility = View.INVISIBLE;
        ln1!!.visibility = View.INVISIBLE;
        processBar!!.visibility = View.VISIBLE;
        mainViewModel.getOneWeather(nameCity,lang)
        mainViewModel.dataWeatherCity.observe(viewLifecycleOwner) {
            if (it != null)
            {
                titleDetails!!.visibility = View.VISIBLE;
                details!!.visibility = View.VISIBLE;
                ln1!!.visibility = View.VISIBLE;
                processBar!!.visibility = View.INVISIBLE;
                binding.dataWeatherCity = it
            }
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun initLangTemp(context: Context, view: View) {
        tv1 = view.findViewById(R.id.tv1)
        tv4 = view.findViewById(R.id.tv4)
        tv5 = view.findViewById(R.id.tv5)
        tv6 = view.findViewById(R.id.tv6)
        tv7 = view.findViewById(R.id.tv7)
        tv8 = view.findViewById(R.id.tv8)
        tv9 = view.findViewById(R.id.tv9)
        tv12 = view.findViewById(R.id.tv12)
        tvTempMode = view.findViewById(R.id.tvTempMode)

        titleDetails = view.findViewById(R.id.titleDetails)
        details = view.findViewById(R.id.details)
        ln1 = view.findViewById(R.id.ln1)

        processBar = view.findViewById(R.id.progress_circular)

        if (Objects.equals(lang,"vi"))
        {
            tv1!!.text = "Nhiệt độ hiện tại : ";
            tv4!!.text = "Độ ẩm";
            tv5!!.text = "Áp suất";
            tv6!!.text = "Tốc độ gió";
            tv7!!.text = "Hướng gió";
            tv8!!.text = "Tầm nhìn";
            tv9!!.text = "Độ bao phủ";
            tv12!!.text = "Lượng mưa";
        }
        else
        {
            tv1!!.text = "Current temp : ";
            tv4!!.text = "Humidity";
            tv5!!.text = "Pressuare";
            tv6!!.text = "Wind speed";
            tv7!!.text = "Wind Direction";
            tv8!!.text = "Visibility";
            tv9!!.text = "Cloud";
            tv12!!.text = "Having Rain";
        }

        if (unit == 0)
        {
            tvTempMode!!.text = "°C";
        }
        else
        {
            tvTempMode!!.text = "°F";
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(name : String): DetailsWeatherFragment {
            val args = Bundle()
            val detailsWeatherFragment = DetailsWeatherFragment()
            args.putString("NAME_DATA", name)
            detailsWeatherFragment.arguments = args
            return detailsWeatherFragment
        }
    }
}